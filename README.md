
# MenuGuru

Este repositório implementa um sistema fictício (MenuGuru) de gestão de pedidos para restaurantes como parte do trabalho de avaliação do curso de Pós Graduação em Software Architecture da FIAP.

O MenuGuru tem como objetivo principal registrar e acompanhar o status de pedidos para restaurantes, onde o cliente pode realizar seu pedido e acompanhar o status do mesmo até a retirada.

Funcionalidades:
* Cadastro e Identificação de Clientes
* Gestão de Produtos (criação, consulta, edição e remoção)
* Gestão de Pedidos (solicitação, consulta e finalização de pedidos)
* Integração com Mercado Pago para processamento dos pagamentos

## Projetos relacionados

O projeto foi estruturado em 5 repositórios, cada um com o objetivo de armazenar os códigos fontes e scripts de cada estrutura da arquitetura do projeto. Cada repositório pode ser acessado nos seguintes links:
* **Aplicação (backend):** repositório com o código Java da aplicação executada em ambiente Kubernetes (*este repositório*).
* **Banco de dados:** repositório contendo os scripts de Terraform para criação do banco de dados MySQL executado na AWS RDS: [https://github.com/perisatto/menuguru-database](https://github.com/perisatto/menuguru-database).
* **Infraestrutura (Kubernetes):** repositório contendo os scripts de Terraform para criação do cluster EKS: [https://github.com/perisatto/menuguru-kubernetes](https://github.com/perisatto/menuguru-kubernetes).
* **Função Lambda:** repositório com o código fonte Java + template SAM para a criação de função Lambda utilizada para autenticação: [https://github.com/perisatto/menuguru-lambda](https://github.com/perisatto/menuguru-lambda).
* **API Gateway:** repositório contendo os scripts de Terraform para o API Gateway utilizado para expor e garantir validação da autenticação das rotas da aplicação backend: [https://github.com/perisatto/menuguru-api-gateway](https://github.com/perisatto/menuguru-api-gateway).


# Arquitetura

Na versão mais atual do projeto o Menuguru foi estruturado para ser executado em uma arquitetura Kubernetes. Nos exemplos deste README você econtrará o desenho de arquitetura com as necessidades de negócio (desenho de arquitetura de solução) e as necessidades de infraestrutura (desenho de infraestrutura).

## Desenho de Arquitetura de Solução

Abaixo estão representadas as principais funcionalidades de negócio abrangidas pelo projeto do Menuguru.

* Não estão representados os endpoints das APIs do sistema. Para verificar a sequencia de execução de cada endpoint verifique a sessão [Fluxo de Negócio](#fluxo). Você também pode encontrar o Swagger e uma coleção do Postaman na sessão [Operações](#operacoes).
* Em alguns casos uma entidade pode se repetir no desenho com o mesmo nome. Nestes casos se trata da mesma entidade e este padrão foi adotado para facilitar a leitura do desenho.

![Desenho de Arquitetura de Solução - Cadastro de Clientes](/images/DAS_Cadastro_cliente.png)

![Desenho de Arquitetura de Solução - Efetuar Pedido](/images/DAS_Pedido.png)

![Desenho de Arquitetura de Solução - Acompanhar Pedido](/images/DAS_Acompanhar_Pedido.png)

### Desenho de Infraestrutura

Abaixo estão representadas a infraestrutura padrão para o Menuguru. O desenho demonstra a implantação em ambiente AWS mas você pode executar o projeto em um provedor Cloud de sua preferência, realizando as devidas adaptações.

![Desenho de Arquitetura de Solução - Acompanhar Pedido](/images/DIF_AWS.png)

Abstraímos aqui os componentes do Kubernetes (ControlPlane, etcd, etc) para focar apenas nos componentes da aplicação.

### Banco de dados

Por ter os dados bem estruturados e sem alterações significantes previstas na evolução do projeto, o banco de dados relacional funciona muito bem para atingir os objetivos do projeto, não havendo necessidade de se optar por outros tipos de banco de dados. Foi adicionado também um banco de dados de documentos que entra para armazenar as informações relativas à pagamentos, possibilitando receber informações de diversos provedores e armazená-las em um único repositório independente do formato.

Ainda assim entendemos que em um cenário com volume de requisições para gravações e consultas (o que não é o caso deste sistema de controle de pedidos de restaurante) poderíamos utilizar um banco de dados Colunar, mantendo a visão mais estruturada dos dados e aumentando a performance do projeto.

Abaixo o modelo de dados do banco de dados escolhido (que também está disponível no README do projeto principal):

![Modelo de dados](/images/MER.png)

# Guia para execução do projeto

* [Docker](#docker)
* [Kubernetes](#kubernetes)

## <a name="docker">Docker</a>

### Pré-Requisitos

* JDK 22 ou mais recente (<https://jdk.java.net/>)
* Apache Maven versão 3.9.6 ou versão mais recente (<https://maven.apache.org/download.cgi>).
* Ambiente Docker

Certifique-se que a variável JAVA_HOME e M2_HOME estão apontadas corretamente para as versões indicadas/mais recentes.

### Execução

Após realizar o download deste repositório, entre na raiz do projeto e configure o arquivo de variáveis de ambiente com a senha a ser utilizada para o usuário root do banco de dados, substituindo "**password**" pela senha escolhida (o valor deve ser informado entre aspas):

```
$echo MYSQL_ROOT_PASSWORD="password" >> .env
$echo MYSQL_PASSWORD="password" >> .env
```

>Caso o arquivo .env já exista no diretório e você queira configurar uma nova senha para o banco de dados, será necessário apagá-lo antes de configurar as variáveis de ambiente conforme instruções acima.

Com as variáveis de ambiente configuradas é hora de iniciar a aplicação utilizando Docker. Na raiz do projeto utilize o comando "docker-compose" para iniciar a aplicação:


``` 
$docker-compose up
```

Verificando no painel de controle do Docker você poderá checar se a aplicação está pronta para uso.

## <a name="kubernetes">Kubernetes</a>

Este projeto foi estruturado para ser implantado em Kubernetes através do uso de Helm Chart.

### Pré-Requisitos

* Helm
* Instância MySQL 8
* Ambiente Kubernetes (Docker Kubernetes / Minikube / AKS / EKS / GKS, etc)

### Execução

1. Execute o script de preparação do banco de dados em sua instância MySQL 8. O script pode ser encontrado no arquivo [data.sql](/mysql/data.sql) deste projeto.

> Este projeto foi idealizado para ser executado com uma instância MySQL em AWS RDS para garantir a escalabilidade e resiliência da aplicação. Caso deseje, você pode criar um Pod de MySQL usando os arquivos de manifesto existentes no diretorio [kubernetes](/kubernetes/) através do comando abaixo:
>
>   $kubectl apply -f ./kubernetes
>
> Não se esqueça de alterar os valores das variáveis "mysql_user" e "mysql_password" no arquivo [menuguru-secret.yaml](/kubernetes/menuguru-secret.yaml).
>
> Caso opte por criar o Pod desta maneira não será necessário executar o passo 1.

2. Ajuste os valores das variáveis no arquivo [values.yaml](/menuguru-helm-chart/values.yaml)
3. Execute o comando para instalação (altere a tag "\<release\>" pelo nome da release de sua preferência):
>   $helm install \<release\> ./menuguru-helm-chart
4. Verifique se os pods estão no status "Ready"
>   $kubectl get pods

Após o ambiente estar pronto, você pode executar os comandos conforme descrito na sessão [Operações](#operaçoes).


## <a name="operacoes">Operações</a>

As funcionalidades do sistema MenuGuru podem ser acessadas através de APIs REST. As operações disponíveis podem ser consultadas através do contrato disponível em linguagem Openapi 3.0.* (Swagger) na pasta [Swagger](https://github.com/perisatto/menuguru/blob/master/src/main/resources/swagger/menuguru.yaml) deste respositório.

> Para consulta do contrato da API de forma mais amigável, utilize um visualizador Openapi disponível em <https://editor-next.swagger.io/> ou outra ferramenta de sua preferência. 

Após o ambiente configurado e a aplicação executando, as operações podem ser acessadas utilizando API Client. Neste repositório você pode encontrar uma coleção atualizada das APIs para serem utilizadas no API Client [Postman](https://www.postman.com/). Esta coleção está disponível na pasta [postman](https://github.com/perisatto/menuguru/blob/master/src/main/resources/postman/MenuGuru.postman_collection.json) deste repositório.

### <a name="fluxo">Fluxo de negócio</a>

Para facilitar o entendimento da sequencia de execução das operações das APIs REST disponibilizamos um diagrama de sequência que simula o cenário de realização de um pedido, partindo de um cliente não cadastrado que tenta se identificar com CPF. O diagrama de sequência pode ser acessado através [deste link](https://bitily.me/cHagP).

# Demonstração

Para demonstrar a arquitetura do projeto e a execução do mesmo em ambiente AWS, disponibilizamos o video no link abaixo com a explicação do projeto e de sua execução.

* [YouTube](https://youtu.be/0e_d6BQKFJQ)