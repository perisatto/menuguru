
# MenuGuru

Este repositório implementa um sistema fictício (MenuGuru) de gestão de pedidos para restaurantes como parte do trabalho de avaliação do curso de Pós Graduação em Software Architecture da FIAP.

O MenuGuru tem como objetivo principal registrar e acompanhar o status de pedidos para restaurantes, onde o cliente pode realizar seu pedido e acompanhar o status do mesmo até a retirada.

Funcionalidades:
* Cadastro e Identificação de Clientes
* Gestão de Produtos (criação, consulta, edição e remoção)
* Gestão de Pedidos (solicitação, consulta e finalização de pedidos)
* Integração com Mercado Pago para processamento dos pagamentos

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

# Guia para execução do projeto

* Fase 1: [Docker](#docker)
* Fase 2: [Kubernetes](#kubernetes)

## <a name="docker">Docker</a>

### Pré-Requisitos

* JDK 22 ou mais recente (<https://jdk.java.net/>)
* Apache Maven versão 3.9.6 ou versão mais recente (<https://maven.apache.org/download.cgi>).
* Ambiente Docker

Certifique-se que a variável JAVA_HOME e M2_HOME estão apontadas corretamente para as versões indicadas/mais recentes.

### Execução

Após realizar o download deste repositório, entre na raiz do projeto e configure o arquivo de variáveis de ambiente com a senha a ser utilizada para o usuário root do banco de dados, substituindo "**password**" pela senha escolhida (o valor deve ser informado entre aspas):

    $echo MYSQL_ROOT_PASSWORD="password" >> .env
    $echo MYSQL_PASSWORD="password" >> .env

>Caso o arquivo .env já exista no diretório, será necessário apagá-lo antes de configurar as variáveis de ambiente conforme instruções acima.

Com as variáveis de ambiente configuradas é hora de iniciar a aplicação utilizando Docker. Na raiz do projeto utilize o comando "docker-compose" para iniciar a aplicação:

    $docker-compose up

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

Para facilitar o entendimento da sequencia de execução das operações das APIs REST disponibilizamos um diagrama de sequência que simula o cenário de realização de um pedido, partindo de um cliente não cadastrado que tenta se identificar com CPF. O diagrama de sequência pode ser acessado através [deste link](https://sequencediagram.org/index.html?presentationMode=readOnly&shrinkToFit=true#initialData=C4S2BsFMAIFlIHYFcDiSBOToFpoDlIB3aAeXQBNJ1oAxcAe0ICgmBDAY2HuoGIBzdJEQAuAAwA6AKzQAwuBCJgkNp27Qeg8mKmz6ALxAIAFqxVdeAI3BJI26QCFW4dgGP6LALasl6EE8oAziB8CCDs9AgBtKIAgjH2tOgRwNiI5NAxAA6ZLJms6KDsIHkIwAL0SJnqTmGQVjZwiEh8GFgAbgCM0AAUACL0HoYg9AEAlEx5BWHFrKWySAFcHlQT+YUzcwAKSeRInAGrU0UlwKQUK2me3lR+4IHBoeGR0QDsoo3o7Kzk9NCbrHx3Ew5ApSpBsAA+GhJUqpBDpLKZYTQADKkCgRQirGgIEopRAADMwvlsAEYDBDATuF5ZJsaGYQG1rolknCEdkmNDWWkMtlITIFksqMIUABRAAq0AA9MtkC1MFLOlL2IKBlR7ABPGR0qUAbx+KtlwDwSA8FioAF8GUylPNFmr0JyYSkeYiADzYbAC+3LdDIgAS4vFm2gABZRKGeiDFDAEG5oIgnsB0N96ONKBxQDaYN6hY7o2CPdgubDXdlkQBJBBU9A09jyGM4hCQAAeIEWMaYGc4jOZJZd8N5OQLSkh-bZQ+RaIxwwQ2K+5FYixT1HroNtFOr1Oxi5+B0zvdt47LOWPg8R-NVvuEmxIKMlMqa8qQio6yqvKwP2btead3PP2RFrmDoBkGIYAEyiF03QjjA7C+Km6aQF+zLAb6f6lgBmRjs6E6IsIMS1qweiINiFbpLBGEDuy2EQts9C7PsIoStKsrNK0r5SpkOx7MAAQAPwAPp5HwkAALwdAAZIJQQkWJkiiJJwAapk4kADIxHgMj+qK1rMvRjF8VReGAZ6Bm8QEwiBsG0CQe83Rqe2pyUNA3EMUgXBjF2yE9t+5n7MCDaFp6Z40cIoptua0D0JkADn8YuUMRT5K5kDkLiIwJtA8iLNibm7J50CLtAXxKICCHQBpWk6d5KFHrhJ6Beu4JQg1WHCNOkCYnO0WuTxXBFb8oCZL8VXabpdUwKFQ7GSeOH-mFhFeCRPXkX8-W-DAACKSCzKAu7KNNF50TxTFig+bHPpx+UWUJIniVJMkgHJClKSp4n2KK9gVr0MR6ba-lGUdpnYIDlnWRBoj2Y5ixFTAN2eUhk3re5AWwUWwNIjDwB5RtUTFaVkDlX40Cfd9v21b5fZtTRTUxvNmFhZ13XYr8CO-D80DDb8ZM-X9yOY7NWEM9Rk5LcRpHQGt5kDdtu34gdQs0ZCYPMRdT4cUqN37HdAIPdJsnia9ymqWJMQyCQsCbJp-oxLAop4OKJD-TAYNK0ORaqxDtlQz02Nw317mI5TWb6adRnoyFNOTtjuNB5lBPXMT2Lm5b1t4Lb9uO873ah-VC0zbBIsmUizOzqzgcFRzQ3FL8qdWzbdsO07LssozM2Y8XJ4EURK1kekMubdAO17bi3yHdHx2q+drEawqWvhwJwl6xJBvPUbikm+JKIkPYABKor2yi-NUwDi-u+6ZmL1ZYE+9DTkB+zXm54ervn5HxaT+WseVx5CfzknbgJMd770PqKY+IdX5t1FoiOmYIu7tTLliXq7NBpc1rqiXeB8j4nzzlNL+p4CEIMWr3SW0sNpZRHgrcecClAYwIciCKIAoqCACKaauqV0o-Egd+QWRdWoF3wroasIAiKcIyq3QWncIRkEoH6W895Z5yk1m+bgcj9ynxgLIlY2j0CQngJ8VMfwAT0GRAoh8hh7SCClGoqgAQpQAEd0BSjJOAKAzjwhuK6uYexuo8gaiNIJBYVByIWi4iMPUI0AiCXCJQMJTiNF4I+AuX4-xARMF0UWAxKTjGAhvjZSC0FtEeEfgCVgRpfjwT8IuC+INdH5MhtBTYaUMolQQj8AANKleEjYRLlMUGmHhzJdG0MgPQwR5Yqw1hpDFWKABXyA+N4ZlIqaM-RVAclpNMaKAkkBQ6uRWQM0ZWSNlGK2cIGQEQiRiL6as7JZyTGQnqeYpR7F56qPOOgXxtj0ChOVFc0RHh-gBMUK3EZ9zOZbKLPU72dkehpP6aUSpAKiI-CRpos4cihm2ghakkxTAED0FtPQNoVBoElwIpkeQXx4q-AsAsL4AclAeCiQmEpzKokAB1CXQFysABY0BvAAEvoAOIaL8PlArOaqS4fQblZIABW2ID4yC+j9EgByUyCv5TUPQKVsRKHAKwbl4QSnzhRV4WlaDblHOkfUmej5lHvJsZ875nzQlgs+bUzI0LPkNLvvClpnNBDsDqBldFSSRmR0xsIKZ25eU435fjNmQb3Av14QQ4E+hDAmGIWLHV8g9XiM5pAEp3FICTHcALTNdq-XW3FP6V5V0lQ-LdXIj1yMRmY19XI-1cLugokTQK7wu1C1GMmNiQ+glNgH2tnvEgEaoFRuzcYVg4z274TjWIkdurWBWulam7F+CJnDhXbmgRG7ywxALc9PKqbA5IskTWohMi60xAbU2lRLr1F6h+R2jFXaCE9uFLC32A6h1RB3WO6V+RsQzpINnRd35l0GFXeumBkytzbpvXq-dKbZVHvJY1fhMbLmRCQOAHG0AiRGsfqmxJUCpEvvtSxR1byXwLwrbB0AEQdqQBsJ6rF3bPQwtvv2n+LkZUZSiD1GjrAkPDK9R-GNW6aSSuTcWtNPkkl8KCqOCEsFhB7z2aIiuUnuGOGcG4PN+Fr2jtvZp6jhhd3cOrSe71Ty30frY82j5P7dR-vIFaTtXrhPYFEwUsDg6R2QZw+O2DtAKx4BiGpCsAAtGIvQF2EZGZZ1w9B0MUtU9iKDt68OacI4LFgFgiVLEmOsE4AQgA).

# Demonstração

Para demonstrar a arquitetura do projeto e a execução do mesmo em ambiente AWS, disponibilizamos o video no link abaixo com a explicação do projeto e de sua execução.

* [YouTube](https://youtu.be/0e_d6BQKFJQ)