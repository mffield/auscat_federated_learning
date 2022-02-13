function [param, received_parameters]=ParseMessage(message,param,client_index)

parameter_names = fieldnames(param);

% remove header of message
message = message(find(message==':',1,'first')+1:end);
if ~isempty(find(message==':',1,'first')) % then we have a client to master message
    param.client_name(client_index) = {message(find(message==':',1,'last')+1:find(message==';',1,'first')-1)};
    training_centers = strsplit(param.training_centers,',');
    msg_client_index = find(ismember(training_centers,param.client_name(client_index)));
    if  isempty(msg_client_index)% && param.test_model==1
        testing_centers = strsplit(param.testing_centers,',');
        msg_client_index = find(ismember(testing_centers,param.client_name(client_index)));
    end
else % we have a master to client message
    msg_client_index=client_index;
end

% split message string based on delimiter, this case it is semi-colon
message=strtrim(strsplit(message,';'));
message=message(1:end-1);
% find the indices of the entries that correspond to any parameter names in the model.
param_index = zeros(1,length(message));
for j=1:length(parameter_names)
    param_index = param_index | cellfun(@(x) strcmp(parameter_names{j},x),message);
end

% for each parameter found place the subsequent values into the model structure.
param_indices = find(param_index);
for j=1:length(param_indices)
    if j==length(param_indices)
        temp = message(param_indices(j)+1:end);
    else
        temp = message(param_indices(j)+1:param_indices(j+1)-1);
    end
    received_parameters = {message{param_indices}};
    num_clients = length(temp)/size(param.(message{param_indices(j)}),1);
    if iscell(param.(message{param_indices(j)}))
        if ischar(param.(message{param_indices(j)}){1})
            param.(message{param_indices(j)}) = temp;
        end
    else
        % if the parameter exists in parameter_names list then we collect
        % parameters per client and enter into allocated column.
        if ~isempty(msg_client_index) && (any(ismember(message{param_indices(j)},param.parameter_names)) || any(ismember(message{param_indices(j)},param.setup_parameters)))
            %disp((message{param_indices(j)}))
            temp1 = reshape(cell2mat(cellfun(@(x) str2double(x),temp(:),'UniformOutput',false)),size(param.(message{param_indices(j)}),1),num_clients);
            if num_clients==1 % parameters from client
                param.(message{param_indices(j)})(:,msg_client_index)=temp1(:,1);
            else % broadcasted parameters from master
                param.(message{param_indices(j)})(:,1)=temp1(:,msg_client_index);
                
            end
        
        else
            temp1 = reshape(cell2mat(cellfun(@(x) str2double(x),temp(:),'UniformOutput',false)),size(param.(message{param_indices(j)}),1),num_clients);
            param.(message{param_indices(j)}) = temp1; %(:,msg_client_index) 
        end
    end
    
    
end
end