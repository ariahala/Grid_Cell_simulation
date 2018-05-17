function [ output_args ] = plotGridCell( p )

fileID = fopen(['/Users/mac/Desktop/Everything/Work/Neuroscience/Grid cell Project/Data_No.' num2str(p) '.txt'],'r');
sizeA = [3 Inf];
formatSpec = '%f %f %f';
A = fscanf(fileID,formatSpec,sizeA);
v = size(A);
for i=1:v(2)
    B((A(1,i)+2)/2,(A(2,i)+2)/2) = A(3,i);
end
C = xcorr2(B);
g = size(B);
for i=1:g(1)
    for j=1:g(2)
        x = 2*i;
        y = 2*j;
        
        if ( norm ([x-62.5,y-62.5]) > (125/2) )
            O(i,j) = 0;
        else
            O(i,j) = 1;
        end
    end
end

Q = xcorr2(O);
q = size(Q);
for i = 1:q(1)
    for j = 1:q(2)
        if ( Q(i,j) < 800 )
            Q(i,j) = 800;
        end
    end
end
result = C./Q;

 figure
 subplot(2,1,1)       % add first plot in 2 x 1 grid
 colormap('jet')
 imagesc(B)
 colorbar
 title(['receptive field neuron ' num2str(p)])
 
 subplot(2,1,2)       % add second plot in 2 x 1 grid
 imagesc(result)
 colorbar
 title('autocorrelogram')

result = result / max(max(result));

end

