
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

Para facilitar o entendimento da sequencia de execução das operações das APIs REST disponibilizamos um diagrama de sequência que simula o cenário de realização de um pedido, partindo de um cliente não cadastrado que tenta se identificar com CPF. O diagrama de sequência pode ser acessado através [deste link](https://sequencediagram.org/index.html?presentationMode=readOnly&shrinkToFit=true#initialData=C4S2BsFMAIFlIHYFcDiSBOToFpoDlIB3aAeXQBNJ1oAxcAe0ICgmBDAY2HuoGIBzdJEQAuAAwA6AKzQAwuBCJgkNp27Qeg8mKmz6ALxAIAFqxVdeAI3BJI26QCFW4dgGP6LALasl6EE8oAziB8CCDs9AgBtKIAgjH2tOgRwNiI5NAxAA6ZLJms6KDsIHkIwAL0SJnqTmGQVjZwiEh8GFgAbgCMTHkFYcWspbJIAVweVN35hf2DAApJ5EicARO9RSXApBTjaSxyCqWQ2AB8NEmlqQjpWZnC0ADKkFBFEazQIJSlIABmYfnYATAYIYvtwvLIZjQzCA2t4YKdkhcrtkmPDzmkMtljjJhqMqMIUABRAAq0AA9GNkC1MKTOqT2Dj6GN0PYAJ4yCGkgDe5Ho9IpwDwSA8FioAF8oTClEMRozxqiUujrgAebDYbEypm3AASRKJM2gABZRAboAAKPaKGAINzQRDhUroVg8gCUTEoHFAkpg6tx6CYFoOKuw8sRGJu0AAkggQegwex5Ja3ghIAAPEAjS1uyAe6GwxIIxXIgNKY4hwvhh5PEAvaDsJ2sEaO6jx-ZSoHR0GvchO+jLHNe-Noy5hlFnBXD65YhmamYkO4k8lNKlIGkdOnT8b9vM+2V+ssT7JBnea6A6vXQABMog6ZuLMHYvh7rvdnFzUuPcrHocnJy-5eEMSxqweiIK8EbpHeo4FgemTHHM9ALEs+LEmSFLNK0q6kpk8yLMAAQAPwAPp5HwkAALwdAAZIRQQgWRkiiJRwAspk5EADIxHgMhagSEp5vBiF4VBQ5IpkQYCbhATCGe+pXqIZpsemGyUNA2EIUgXABM+2avgOElLP6CaBqq+6icIBJpiK0D0JkADnNoqYYfT5KpkDkO8va2tA8gjK8akLJp0DdrWsJ8NwfjQBxXE8VmW5SqZI53qWf4wcIlaQM8CCvPQqk4VwQU5aAmQ5VF3G8XFcIpaJwnjqJyXQWZgFeCBWWRukEn5TAACKSADKA3aUDV36Ykc+l4chC5ocumH+ZJREkeRVE0SAdEMUxLHkfYBL2BGAAiMR8VKY3LAlyqqsd0m6rJojyaaikjEFMCzZp2kVdAx2Ga2kBBqd2TCPdwB+XlnnBXWShhY+0BbTt+2xbpea-TkSW-g1YZpY8GXVq1OXPTlPLQEVOXQ3tB1vYjQ3lvVIlo01wGgW173A15PV9e8TrKIjcE4UhhKTUuGG0rNSzzawpEUdRtHkWtzGsWRMQyCQsAzJxWoxLABJ4ESJCHTAH2I+J3PjTJl43QpSmPbl6kvXDnr8Ybyx3j9VVowDQNWyDrxg5AEMRfLivK3gqvq5r2svrb8XO9cn2WlTtVo+lmXZZbAV44VxQ5X7Ssq2rGtazrg5x1HnMo9T1wAUBLVge1TPdb1nwDRzkcjRdvOofz1KC-bIti0tkv0YxMvkXcJD2AASgS6t3KT8NHfbFMwQb6lIcbclmw9Km41pNtvrrc+OyZTc3K7ycaR7IXg+FrzD2PE8ElP28DuTyOI+jVY1jjTP44T9wj+Pk-T+HSqqMi6H1jsNG4tNK4Mw6jlWurMG7R2MsGQ+twLIgCsoIAIQpU6uXcjyB+CND6IJLCXQuf1dDRhAEBXBHl87k2LmQSg6BhCznnG3SkAs1zcCYX2GeMBGGfmAYeVUAjmEr2vGaGYbkPK1kfDyAANK5S4iYSKsH5PQV6fDNhMOId9A+QibhRhjGCGytkACvkAogb1FmoxQ7gw47wLuA3RYD-wEi+JAW2qkbHqLoUQhhWxmGsL5hwjuXDAkBC5Nwqg4FRR0iMBlAA1hUYA+dRHz1EkGURl1zyr1NFIvBOVBDsDqB5TRgDtHjH3i-IxnZoC+WAMMAqND8EOMfkQmQ+hDAmFcalGIjSah6D8tI-GkAPC5UgD0dwZN-GgKONk5WRItTsPQmE0k0T0CRM5Bs2JaTAkZLDFkwJOTrq3TuIDRpURvC9XkEM-GPRXgT0IjMceytR4kHKY49JnSDDGFYE7AxwhanUOuYM1g9kcGsUKQQiOBj-RdL+b0xqAzbnDMKZbUo0ytH0LmQsmISyVnTVpBsrZOzyDijeuk-WIjjniLORcppoLUX3PyK8F5JAQ6fIHN8hFJgAWlz+sCsETKVrgocjjEZ9idIVKfkZEhL9OmRCQOAQG0AfjgC7E9SVvCZWzIMccbJrdFyhJXJ3SZrLQARB6pAGweydHUuwNkula9VUb21dAVq6rWBcrzN8uVejkGAqFa8BpTT7mSphUAgVSN-VYn9cIUeniqFJyhbQxwzg3BIppii0VLSco-CysyrFuq4UBKYSw-FyzjWrNNeEnhUTAm7Mpfsh1Tqrom3pdcq5Oa7k41ZbQCMeAYhsQjAALRiLtD5kbKl+nTa4eg-KyGGI7CCntYrIURraYQuFLALD0GAKMHoUx1gBCAA)

# Demonstração

Para demonstrar a arquitetura do projeto e a execução do mesmo em ambiente AWS, disponibilizamos o video no link abaixo com a explicação do projeto e de sua execução.