 
% mcc -m LocalLogReg.m -d ./output  -a ./src -a '.\@MPIService'

%LocalLogReg('C:\DistributedLearning\WebFramework\MPIClient.properties','MPIClient')

function LocalLogReg(MPI_config,MPI_name)

config = readConfigFile(MPI_config);
if ~isfield(config, 'MPIClientPort')
    config.MPIClientPort='80';
end
svc=MPIService(['http://localhost:' config.MPIClientPort '/' MPI_name '/MPIClient'],...
                ['http://localhost' config.MPIClientPort '/' MPI_name '/MPIClient?wsdl']);
disp(['Client starting from ' config.clientName])
startedMPI = initialiseClientMPI(svc);
if strcmp(startedMPI,'false')
    disp('MPI failed to start')
else
    disp('Initialisation of MPI successful')

disp('Waiting for central algorithm to start');    
while strcmp(isMasterOn(svc),'false')
    pause(0.2);
end
    disp('Central algorithm is ON');

    ClientIndex=str2double(addClient(svc));

    if ClientIndex==-1
        disp(['Can not add Client ' config.clientName])
    else
        disp(strcat(['Client ' config.clientName ' added with index: '],num2str(ClientIndex)))

        model = init_logreg_model('local',[],[],[],[],[]);
        prev_itr=-1;
        data=[];

        
        [itr, model, ~, central_message] = Client_Receive_from_MPI(svc,prev_itr,model);

        while ~strcmp(model.state,'terminate')

            if contains(central_message,'algorithm;logreg')

                [local_message,model,data]=logreg_local_update(config,central_message,model,data);

                % Sending back local sum and number of data points to compute mean
                local_message = strcat(config.clientName, ';', local_message);

                sendReplyToMaster(svc, itr, local_message); prev_itr=itr;
                disp(strcat('Client ',num2str(ClientIndex),'  sent reply itr ',num2str(itr),' :  ',local_message));

                % Wait until central algorithm has an update
                [itr, model, ~, central_message] = Client_Receive_from_MPI(svc,prev_itr,model);

            end

        end
        sendReplyToMaster(svc, itr, 'null');
        disp(strcat('Client ',num2str(ClientIndex),'  sent reply itr ',num2str(itr),' :  null'));
 
    end
end

