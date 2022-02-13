

from zeep import Client
import numpy as np
import json
import time

mpi_name = 'MPIClient1'
client_name = 'client2'
mpi_client_port='80'

mpi = Client('http://localhost:' + mpi_client_port + '/' + mpi_name + '/MPIClient?wsdl')


print('Client starting from ' + client_name)

if mpi.service.initialiseClientMPI()==False:
    print('MPI failed to start')
else:
    print('Initialisation of MPI successful')
    
    timeout = time.time() + 60*1 
    while mpi.service.isMasterOn()==False:
        print('Waiting for central algorithm to be started')
        if time.time() > timeout:
            print('Connection Timeout.')
            break
        
    print('Central algorithm is ON')
        
    client_index=mpi.service.addClient()
    model = {}
    if client_index==-1:
        print('Failed to add client')
    else:    
        print('Client ' + client_name + ' added with index: ' + str(client_index))
        model['state'] = ''
        
        timeout = time.time() + 60*1 
        while model['state'] != 'terminate_function':
            model['client_name'] = client_name
            prev_itr=-1
            
            if time.time() > timeout:
                print('Connection Timeout.')
                break
            
            timeout2 = time.time() + 60*1
            while mpi.service.readMasterItr() == prev_itr:
                if time.time() > timeout2:
                    print('Connection Timeout.')
                    break
        
            itr = mpi.service.readMasterItr()
            
            model['state'] = 'terminate_function'
            
            central_message = mpi.service.readMasterMessage()
            print('Message received from central algorithm' + central_message)
             
            central_model = json.loads(central_message)
            
            if central_model['state']=='initialise':
            
                model['mean']=-2
                model['covariance']=1
                model['N']=120
                print('Return message:' + json.dumps(model))
                mpi.service.sendReplyToMaster(itr,json.dumps(model))
                
                
                # To be continued! OF course will be creating subroutine for some of the standard steps. Then the code will be more compact.
