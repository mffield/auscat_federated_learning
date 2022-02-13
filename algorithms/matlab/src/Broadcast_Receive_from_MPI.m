function [model, itr] = Broadcast_Receive_from_MPI(svc,master_message,num_clients,itr,model)

itr=itr+1;
publishMessageToClients(svc,master_message,itr); % Send the model update to all clients in network

% Wait until all clients have received model
while strcmp(didAllClientsReply(svc,itr),'false'); %pause(1);
end

for i=1:num_clients   % Collect the updated model from each client
    client_msg=getClientReply(svc,i-1);
    model = ParseMessage(client_msg,model,i);
end

end