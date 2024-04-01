# Aplicativo de Lista de Contatos Android

Este projeto foi desenvolvido como parte do meu aprendizado em desenvolvimento Android nativo, buscando colocar em prática os conceitos e técnicas que adquiri até o momento. O aplicativo consiste em uma aplicação CRUD (Create, Read, Update, Delete), que gerencia uma lista de contatos.

## Funcionalidades Principais

- **Listagem de Contatos:** A tela principal do aplicativo exibe uma lista de contatos previamente cadastrados. Utilizei uma RecyclerView para tornar a visualização dos contatos mais dinâmica e eficiente.

- **Adicionar Contato:** Ao pressionar o botão flutuante de adicionar, os usuários podem inserir um novo contato fornecendo seu nome, e-mail e número de telefone. Implementei um formulário simples para capturar essas informações.

- **Editar Contato:** Os usuários podem editar um contato existente simplesmente pressionando e segurando o item da lista correspondente. Isso abrirá um diálogo de contexto onde a opção de edição estará disponível.

- **Excluir Contato:** Além da edição, os usuários também têm a opção de excluir um contato da lista. Ao pressionar e segurar o item da lista, será exibido um diálogo de contexto, permitindo que o usuário escolha a opção de exclusão.

- **Armazenamento Local com Room:** Para persistir os dados dos contatos, integrei a biblioteca Room, que é um ORM (Object-Relational Mapping) para SQLite no Android. Isso permite que os contatos sejam armazenados localmente no dispositivo de forma eficiente.

- **Alertas de Diálogo:** Implementei alertas de diálogo para fornecer feedback ao usuário em diversas situações, como confirmação de exclusão de um contato.
