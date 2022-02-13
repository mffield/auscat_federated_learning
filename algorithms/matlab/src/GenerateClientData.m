function  [A,x,y]=GenerateClientData(M,N,dim,seed)

% %%%% Load some prepared data example
% data = csvread(['SVM_code\rand_set' num2str(seed) '.csv']);
% 
% n=size(data,2)-1;
% x = data(:,2:4)';
% y = data(:,1)'; y(y==0)=-1;
% A =[-((ones(n,1)*y).*x)' -y']; 


% %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% %%%% randomise the data
rng(seed);

pos_mean = 0.9*randn(dim,1); pos_var = 0.2*randn(dim,1);
neg_mean = 0.9*randn(dim,1); neg_var = 0.5*randn(dim,1);
% positive examples
Y = [repmat(pos_mean',N,1)+repmat(pos_var',N,1).*randn(N,dim)];

% negative examples
X = [repmat(neg_mean',M,1)+repmat(neg_var',M,1).*randn(M,dim)];

x = [X; Y]';
y = [ones(1,N) -ones(1,M)];
A = [-((ones(dim,1)*y).*x)' -y'];



% 
% rand('seed', seed);
% randn('seed', seed);
% n=2;
% 
% Y = [1.5+0.9*randn(1,0.6*N), 1.5+0.7*randn(1,0.4*N);
% 2*(randn(1,0.6*N)+1), 2*(randn(1,0.4*N)-1)];
% 
% % negative examples
% X = [-1.5+0.9*randn(1,0.6*M),  -1.5+0.7*randn(1,0.4*M);
% 2*(randn(1,0.6*M)-1), 2*(randn(1,0.4*M)+1)];
% 
% x = [X Y];
% y = [ones(1,N) -ones(1,M)];
% A = [-((ones(n,1)*y).*x)' -y'];


%%%%% Load real data 

% postgres connection



end