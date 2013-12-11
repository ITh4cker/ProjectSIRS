# Creates N processes that will run a infinite cycle
N=20

for i in 1 .. N
do
	fork
done

while [ true ]
do
	;
done
