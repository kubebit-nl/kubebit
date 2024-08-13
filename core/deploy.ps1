Start-Process "mvn" -ArgumentList "clean -f ./source/pom.xml" -Wait -NoNewWindow
Start-Process "mvn" -ArgumentList "package -f ./source/pom.xml" -Wait -NoNewWindow
docker build -t 127.0.0.1:5001/kubebit:latest .
docker push 127.0.0.1:5001/kubebit:latest
kubectl apply -f ./source/infrastructure/target/classes/META-INF/fabric8/projects.kubebit.nl-v1.yml
kubectl apply -f ./source/infrastructure/target/classes/META-INF/fabric8/releases.kubebit.nl-v1.yml
kubectl apply -f ./source/infrastructure/target/classes/META-INF/fabric8/templates.kubebit.nl-v1.yml
helm upgrade --install kubebit ./helm --install --namespace kubebit --set git.commit=$(New-Guid)
