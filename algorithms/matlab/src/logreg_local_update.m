function [local_message,model,data]=logreg_local_update(config,central_message,model,data)
% Logistic regression - implemented through conjugate gradient ascent
%
% beta: step size
% u: search direction
% g: gradient of the weights
% H: Hessian
%
local_message=[];

[model, ~] = ParseMessage(central_message,model,model.alg_client_index);

if strcmp(model.state,'initialise')
           
    data = LoadCSVData(config,model.features,'survival_time');
    [~, model.localN] = size(data.x);
    
    model.localMin = min(data.x,[],2);
    model.localMax = max(data.x,[],2);
    model.localSum = sum(data.x,2);
    % send back values for normalisation, sum(to compute mean) and min and max (to compute range)
    local_message=['localSum;' num2str(model.localSum(:)','%.10f;')...
                    'localMin;' num2str(model.localMin(:)','%f;')...
                    'localMax;' num2str(model.localMax(:)','%f;')...
                    'localN;' num2str(model.localN,'%i;')]; 

                
elseif strcmp(model.state,'updateGradHess')    
    
    X = data.x; y=data.y;
    [~,N]=size(data.x);
    
    X = X' - repmat(model.Mu(:)',N,1);
    X = (X./repmat(model.Range,N,1))';
    
    X = [ones(1,length(y));X];

    w = model.w; u=model.u;
    
    % calculate gradient
    model.localGrad = (ones(1,length(y)) - 1./(1+exp(-w*X.*y))).*y*X';
    % calculate Hessian    
    A = (1./(1+exp(-w*X.*y))).*(1-1./(1+exp(-w*X.*y)));
    model.localHess = -(sum(((u*X).^2).*A));

    %calculate likelihood
    model.localLik  = -sum(log(1+exp(-w*X.*y)));
    
    y_=double(1./(1+exp(-w*X))<0.5); y_(y_==0)=-1;
    model.sumError = sum(y-y_==0);
        
    local_message=['localGrad;' num2str(model.localGrad(:)','%.10f;')...
                    'localHess;' num2str(model.localHess,'%f;')...
                    'localLik;' num2str(model.localLik,'%f;')...
                    'sumError;' num2str(model.sumError,'%i;')];
   

end
end
