function [svc, number_of_clients] = ServerConnectionSetup(MPI_name,participating_clients,initialise)

javaaddpath([pwd '\+wsdl\mpiclient.jar'])
%%% Process input arguments

clients_names = strsplit(participating_clients,',');
number_of_participating_clients=length(strsplit(participating_clients,','));

disp(number_of_participating_clients)
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%% Connect to distribution server
svc=MPIService(['http://localhost:8080/' MPI_name '/MPIClient?wsdl']);
startedMPI = initialiseCentralMPI(svc);
if strcmp(startedMPI,'true')
    disp('Initialisation of MPI successful')
else
    disp('MPI failed to start')
    return
end
if initialise
    initClientInfo(svc);
end

if str2double(getNumberofCurrentClients(svc))~=number_of_participating_clients
    setAllowedClientsList(svc,participating_clients);
    
    setMasterOn(svc);
    
    %%% Wait for each client to also connect to the server
    while str2double(getNumberofCurrentClients(svc))<number_of_participating_clients
        pause(0.2);
    end
end
number_of_clients= str2double(getNumberofCurrentClients(svc));

disp('All clients have connected to the server')


end