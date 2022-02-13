function model = init_logreg_model(mode,model,features,client_names,training_id,testing_id)


%

if strcmpi(mode,'central')
    
    model.d=length(features);% feature dimensions in simulated data test
    model.features = strjoin(features,',');
    model.max_itr=2;
    model.clients_names=client_names;
    model.training_centers_index = training_id;
    model.testing_centers_index = testing_id;
    model.training_centers = strjoin(model.clients_names(model.training_centers_index),',');
    model.testing_centers = strjoin(model.clients_names(model.testing_centers_index),',');
    number_of_training_centers = length(strsplit(model.training_centers,','));

    model.parameter_names = {''};
    model.setup_parameters = {'localN','localSum','localHess','localGrad','localLik','sumError','localMin','localMax'}; % names of setup parameters
    model.state={''};
    model.localN = zeros(1,number_of_training_centers); % number of data points per feature
    
    model.lambda=5;  
    model.beta=1;
    model.g = zeros(model.d+1,1);
    model.localGrad = zeros(model.d+1,number_of_training_centers);
    model.localHess = zeros(1,number_of_training_centers);
    model.localLik = zeros(1,number_of_training_centers);
    model.sumError = zeros(1,number_of_training_centers);
    model.localMin = zeros(model.d,number_of_training_centers);
    model.localMax = zeros(model.d,number_of_training_centers);
    model.localSum = zeros(model.d,number_of_training_centers);

    model.Mu=zeros(model.d,1);
    model.Range=zeros(model.d,1);
    
elseif strcmpi(mode,'local')
    
    if isempty(model)
        model.alg_client_index=1;
        model.training_centers = {' '};
        model.testing_centers = {' '};
        model.features={' '};
        model.setup_parameters = {'localN'};
        model.parameter_names = {''};
    else
        model.d = length(strsplit(features{1},','));
        model.parameter_names = {''};
        model.setup_parameters = {'localN'};
    end
    model.state={''};
    model.w=0;
    model.u=0;
    model.localN=0;
    model.localGrad=0;
    model.localHess=0;
    model.localLik=0;
    model.sumError=0;
    model.lambda=0;
    
    model.localSum=0;
    model.localMin=0;
    model.localMax=0;
    
    model.Mu=0;
    model.Range=0;
end
model.algorithm='logreg';

end


