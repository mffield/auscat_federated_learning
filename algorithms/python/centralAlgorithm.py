'''
Created on 3 Oct. 2018

@author: 
'''

# Testing the MPI client using zeep toolbox

from zeep import Client
import numpy as np
import json
import time

mpi_name = 'CentralMPI'
features = ['GTV', 'fev', 'ecog', 'age_start_rt', 'gender']
# list the allowed clients for this analysis
client_names = ['client1', 'client2']

#center_indices = np.arrange(0, len(clients_names), 1)

mpi = Client('http://localhost/' + mpi_name + '/MPIClient?wsdl')

if mpi.service.initialiseCentralMPI()==False:
    print('Unsuccessful initialisation of MPI')
else:
    print('Initialisation of MPI successful')

    mpi.service.initClientInfo()
    
    if mpi.service.getNumberofCurrentClients() != len(client_names):
        mpi.service.setAllowedClientsList(",".join(client_names))
        mpi.service.setMasterOn()
        print('Waiting for clients to connect.')
        timeout = time.time() + 60*2  # 1 minutes from now
        while mpi.service.getNumberofCurrentClients() < len(client_names):
            if time.time() > timeout:
                print('Connection Timeout.')
                break
    else:
        print('All clients already connected')
    
    if mpi.service.getNumberofCurrentClients() == len(client_names):
        print('All clients connected.')
    
        itr = 1
        print(['Starting algorithm: '])
        
        
        model = {}
        model['state'] = 'initialise'
        model['lambda'] = 1
        model['training_centers'] = client_names
        model['validation'] = 'bootstrap'
        model['algorithm'] = 'logreg'
        model['max_iter'] = 2
        model['features'] = features
        
        
        msg_param = { 'state', 'training_centers', 'algorithm', 'features' }
        model_msg = { key:value for key,value in model.items() if key in msg_param}
        
        message = json.dumps(model_msg)
        print('Central algorithm sends the message: ' + message)
        
        itr=itr+1
        #broadcast_message_to_network(mpi,message,itr,1.0)
        
        mpi.service.publishMessageToClients(message,itr) # Send the model update to all clients in network
        
        timeout = time.time() + 60*1 # set timeout 
        while mpi.service.didAllClientsReply(itr)==False: # Wait until all clients have received model
            if time.time() > timeout:
                print('Connection Timeout. Clients did not all reply in time.')
                break
        
        
        if mpi.service.didAllClientsReply(itr)==True:
            for i in range(0, len(client_names)):   # Collect the updated model from each client
                client_msg = mpi.service.getClientReply(i)
                model['client' + str(i)] = json.loads(client_msg)
                print(model['client' + str(i)])
         
        
        model['state'] = 'terminate_function'
        msg_param = {'state'}
        model_msg = { key:value for key,value in model.items() if key in msg_param}
        message = json.dumps(model_msg)
        itr=itr+1
        mpi.service.publishMessageToClients(message,itr)
        
            # To be continued. Will design subroutines to handle the standard communication steps