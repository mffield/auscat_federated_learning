function [model, lik, error] = update_logreg_model(model)

u=model.u; g_ = model.g; w_ = model.w; w = model.w;
%combine gradients
lambda_vec = [0; model.lambda*ones(model.d,1)]; %do not penalize bias term
g = sum(model.localGrad,2) - lambda_vec.*w;

%combine Hessian terms
uTHu = sum(model.localHess) + model.lambda*(u'*u);

%combine likelihoods
lik = sum(model.localLik) - (model.lambda/2)*(w'*w);
error = sum(model.sumError);
%update w
model.w = w_ - ((g'*u)./uTHu)*u;

model.u = g - u*model.beta;

model.beta = g'*(g-g_)/(u'*(g-g_));

model.g = g;

end