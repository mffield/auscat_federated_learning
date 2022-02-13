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
model = init_logreg_model('central',model,features,clients_names,center_indices,center_indices);

itr=1;  
disp(['Starting algorithm: ' model.algorithm])
% Prepare the first message with initialised model parameters.
model.state='initialise';
central_message=ConstructMessage([{'algorithm'},{'training_centers'},{'state'},{'features'},{'lambda'}],model);
model_ = model;
[model, itr] = Broadcast_Receive_from_MPI(svc,central_message,num_clients,itr,model_);

model.w=randn(model.d+1,1); model.u=randn(model.d+1,1); model.g=randn(model.d+1,1);
L =[]; E=[];

model.Mu = sum(model.localSum,2)/(sum(model.localN));
model.Range = max(model.localMax,[],2) - min(model.localMin,[],2);

for i=1:30

model.state='updateGradHess';
central_message=ConstructMessage([{'algorithm'},{'state'},{'w'},{'u'},{'Mu'},{'Range'}],model);
model_ = model;
[model, itr] = Broadcast_Receive_from_MPI(svc,central_message,num_clients,itr,model_);


[model, lik, error] = update_logreg_model(model);
L = [L lik]; E = [E error];
if lik==-inf %reinitialise
    model.w=randn(model.d+1,1); 
    model.u=randn(model.d+1,1); 
    model.g=randn(model.d+1,1);
    disp(['reinitialised due to lik:' num2str(lik)]);
end
end


model.state='terminate';
central_message=ConstructMessage([{'algorithm'},{'state'}],model);
[~, itr] = Broadcast_Receive_from_MPI(svc,central_message,num_clients,itr,model);


%%%%
