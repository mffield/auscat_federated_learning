% test central algorithm

MPI_name='CentralMPI';
features = {'GTV','fev','ecog','age_start_rt','gender'};
% list the allowed clients for this analysis
% clients_names = {'westmead','liverpool'};  
clients_names = {'westmead','liverpool','christie'};  

center_indices = 1:length(clients_names);

disp('Master algorithm starting')
[svc, num_clients] = ServerConnectionSetup(MPI_name,strjoin(clients_names,','),true);

model = struct;
model = init_model('central',model,features,clients_names,center_indices,center_indices);

itr=1;  
disp(['Starting algorithm: ' model.algorithm])
% Prepare the first message with initialised model parameters.
model.state='initialise';
central_message=ConstructMessage([{'algorithm'},{'training_centers'},{'state'},{'features'}],model);

model_ = model;
[model, itr] = Broadcast_Receive_from_MPI(svc,central_message,num_clients,itr,model_);

model.Mu = sum(model.localSum,2)/(sum(model.localN));

central_message=ConstructMessage([{'algorithm'},{'Mu'}],model);

model_ = model;
[model, itr] = Broadcast_Receive_from_MPI(svc,central_message,num_clients,itr,model_);

model.Cov = sum(reshape(model.localCov,model.d,model.d,length(clients_names)),3)/sum(model.localN);

%%%%
