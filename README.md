# SmartSplit
```
Aplicativo para dividir as contas de forma inteligente. 
Bem útil para rachar as contas de forma proporcional 
entre seus amigos e colegas de trabalho.
```
# Funcionalidades aplicadas
As funcionalidades abaixo foram adicionadas como forma de estudar/praticar

### Checkstyle e PMD
>Plugins de checkstyle e PDM fazem parte do build, quebrando o build caso alguma regra seja violada

### Autenticação com Google-Auth
>Integração com API do Google para autenticação utilizando o tokenId

### GitHub Actions CI/CD
>Utiliza Github Actions para CI/CD. Qualquer push na master, automaticamente executa o build e faz deploy na AWS EC2

### AWS EC2
>Foi criado uma instância no EC2 com IP estático (Elastic IP): `3.22.89.219`

>Caso a instância na EC2 seja reiniciada, foi criado um serviço para que os container sejam iniciados automaticamente no linux. 

>Os arquivos com instruções e a configuração utilizada para executar os containers como serviço na instância da EC2 estão no diretório "aws-ec2-config/"

# SWAGGER
Acesso ao Swagger da aplicação
### Local
> http://localhost:8080/swagger-ui/index.html

### AWS EC2
URL para acesso do Swagger da aplicação rodando na EC2 da AWS:
> http://3.22.89.219:8080/swagger-ui/index.html
