# SmartSplit
- Aplicativo para dividir as contas de forma inteligente. 
  Bem útil para rachar as contas de forma proporcional 
  entre seus amigos e colegas de trabalho.
  
## DEV
- Build
```
mvn clean install
```

## PROD
- Build
```
mvn -Pprod clean install
```

## EC2 POSTGRES
docker run -p 5432:5432 -v /home/postgres:/var/lib/postgresql/data -e POSTGRES_PASSWORD=1234 -d postgres