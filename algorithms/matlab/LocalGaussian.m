
% mcc -m LocalGaussian.m -d ./output  -a ./src -a '.\@MPIService'

% LocalGaussian('C:\DistributedLearning\WebFramework\MPIClient.properties','MPIClient')

function LocalGaussian(MPI_config,MPI_name)

config = readConfigFile(MPI_config);
if ~isfield(config, 'MPIClientPort')
    config.MPIClientPort='80';
end
svc=MPIService(['http://localhost:' config.MPIClientPort '/' MPI_name '/MPIClient'],...
               ['http://localhost:' config.MPIClientPort '/' MPI_name '/MPIClient?wsdl']);

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

        model = init_model('local',[],[],[],[],[]);
        prev_itr=-1;

        local_message=[];
        [itr, model, ~, central_message] = Client_Receive_from_MPI(svc,prev_itr,model);


        if contains(central_message,'algorithm;gaussian')

            data = LoadCSVData(config,model.features,'survival_time');
            [model.d, localN] = size(data.x);
            localSum = sum(data.x,2);
            model.Mu = zeros(model.d,1);

            % Sending back local sum and number of data points to compute mean
            local_message = strcat(local_message, config.clientName, ';',...
                'localSum;', num2str(localSum(:)','%f;'),...
                'localN;', num2str(localN,'%f;'));

            sendReplyToMaster(svc, itr, local_message); prev_itr=itr;
            disp(strcat('Client ',num2str(ClientIndex),'  sent reply itr ',num2str(itr),' :  ',local_message));

            % Wait until central algorithm has an update
            [itr, model, ~, ~] = Client_Receive_from_MPI(svc,prev_itr,model);

            % Compute the covariance statistic
            centredData = data.x - repmat(model.Mu,1,localN);
            localCov = centredData*centredData';

            % Send back local covariance
            local_message = strcat(config.clientName, ';',...
                'localCov;', num2str(localCov(:)','%f;'));
            sendReplyToMaster(svc, itr, local_message); %prev_itr=itr;
            disp(strcat('Client ',num2str(ClientIndex),'  sent reply itr ',num2str(itr),' :  ',local_message));
        end

    end
    
end
end
