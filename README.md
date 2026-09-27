# Organizador de Ferramentas

Aplicativo Android (Java) para controlar o acervo de ferramentas de uma oficina. Com ele é possível cadastrar, listar, editar e excluir ferramentas. Os dados ficam salvos em um banco SQLite acessado pelo **Room**.

Projeto final da Especialização em Tecnologia Java, na UTFPR.
Autor: Rodolfo Ricardo (rodolforicardo@alunos.utfpr.edu.br).

## Funcionalidades

- Cadastro de ferramentas com nome, código/nº de série, localização, categoria, estado de conservação e disponibilidade para empréstimo.
- Lista das ferramentas cadastradas, montada com um adapter customizado.
- Edição e exclusão pelo menu de ação contextual, que abre com um toque longo no item. A exclusão pede confirmação em um `AlertDialog`.
- Configurações salvas com `SharedPreferences`: a ordenação da lista e os valores sugeridos em cada novo cadastro.
- Tela "Sobre", com a descrição do aplicativo e os dados de autoria.
- Dois idiomas: inglês (padrão) e português do Brasil.

## Estrutura do projeto

```
app/src/main/
├── java/com/example/organizadorferramentas/
│   ├── BaseActivity.java               # base comum das Activities
│   ├── ListaFerramentasActivity.java   # tela principal (lista)
│   ├── MainActivity.java               # cadastro / edição
│   ├── ConfiguracoesActivity.java      # configurações do usuário
│   ├── SobreActivity.java              # sobre / autoria
│   ├── Ferramenta.java                 # entidade Room
│   ├── FerramentaDao.java              # operações no banco
│   ├── FerramentasDatabase.java        # banco de dados Room
│   ├── FerramentaAdapter.java          # adapter customizado da lista
│   └── Preferencias.java               # acesso ao SharedPreferences
└── res/
    ├── layout/          # telas e layout do item da lista
    ├── menu/            # menus de opções e menu contextual
    ├── drawable/        # ícones das ações
    ├── values/          # textos em inglês (padrão), cores, temas, arrays
    └── values-pt-rBR/   # textos em português do Brasil
```

## Classes

### Camada de dados (Room)

#### `Ferramenta` — entidade

A classe é anotada com `@Entity(tableName = "ferramentas")` e cada objeto corresponde a uma linha da tabela.

| Campo | Tipo | Descrição |
|---|---|---|
| `id` | `long` | Chave primária gerada automaticamente (`@PrimaryKey(autoGenerate = true)`). Também define a ordem de cadastro. |
| `nome` | `String` | Nome da ferramenta. |
| `codigo` | `String` | Código ou número de série. |
| `categoria` | `int` | Posição no array `categorias_ferramentas`. |
| `localizacao` | `String` | Onde a ferramenta fica guardada. |
| `estado` | `int` | Posição no array `estados_ferramentas`. |
| `disponivel` | `boolean` | Indica se a ferramenta pode ser emprestada. |

A categoria e o estado são gravados como **números**, e não como o texto exibido na tela. Assim, se o idioma do aparelho mudar, os dados salvos continuam válidos. Os métodos `getDescricaoCategoria(Context)` e `getDescricaoEstado(Context)` convertem esses números no texto do idioma atual.

O Room usa o construtor vazio. O construtor com todos os campos é marcado com `@Ignore` e serve para o código do app criar novas ferramentas.

#### `FerramentaDao` — acesso aos dados

Interface anotada com `@Dao`. O Room gera a implementação dela durante a compilação.

| Método | Anotação | Operação |
|---|---|---|
| `inserir(Ferramenta)` | `@Insert` | Grava uma nova ferramenta. |
| `alterar(Ferramenta)` | `@Update` | Atualiza uma ferramenta existente, localizada pelo `id`. |
| `excluir(Ferramenta)` | `@Delete` | Remove a ferramenta. |
| `buscarPorId(long)` | `@Query` | Retorna uma ferramenta pelo `id`. |
| `listarTodas()` | `@Query` | Retorna todas as ferramentas, ordenadas por `id`. |

#### `FerramentasDatabase` — banco de dados

Classe abstrata que estende `RoomDatabase` e é anotada com `@Database(entities = {Ferramenta.class}, version = 1)`. O método `getInstance(Context)` devolve sempre a mesma instância do banco (padrão *singleton*), criada uma única vez sobre o arquivo `ferramentas.db`. O DAO é obtido por `getFerramentaDao()`.

> Por simplicidade, o banco é criado com `allowMainThreadQueries()`, então as consultas rodam na thread principal. Para o volume de dados deste aplicativo isso não causa travamentos perceptíveis.

### Telas (Activities)

#### `BaseActivity`

Classe abstrata da qual todas as telas herdam. Ela sobrescreve `setContentView` para aplicar o espaçamento das barras do sistema e do teclado (*window insets*), evitando que o conteúdo fique escondido atrás delas.

#### `ListaFerramentasActivity` — tela principal

É a tela aberta ao iniciar o app (`LAUNCHER`).

- **Carregamento:** em `onResume`, o método `carregarFerramentas()` busca todas as ferramentas com `listarTodas()` e as ordena conforme a preferência salva (ordem de cadastro, nome ou código). A ordenação por texto usa `Collator`, que respeita os acentos.
- **Toque simples** em um item: mostra um `Toast` com o resumo da ferramenta.
- **Toque longo** em um item: abre o **menu de ação contextual** (`ActionMode`, arquivo `menu_contextual_lista.xml`) com os ícones de **Editar** e **Excluir**.
- **Editar:** abre a `MainActivity` enviando apenas o `id` da ferramenta.
- **Excluir:** o método `confirmarExclusao()` exibe um **`AlertDialog`** com o nome da ferramenta. Somente ao responder "Sim" o método `excluirFerramenta()` remove o registro do banco (`excluir()`) e da lista.
- **Menu de opções** (`menu_lista.xml`): o ícone **Adicionar** fica na barra de ação, e **Configurações** e **Sobre** ficam no menu de estouro (⋮).
- **Retorno do cadastro:** em `onActivityResult`, a tela mostra se a ferramenta foi adicionada ou alterada. A lista é recarregada do banco logo em seguida, no `onResume`.

#### `MainActivity` — cadastro e edição

Formulário com os campos da ferramenta. Tem o botão **Up** na barra de ação e o menu de opções `menu_cadastro.xml`, com os ícones **Salvar** e **Limpar**.

- **Novo cadastro:** quando nenhum `id` é recebido. Se as sugestões estiverem ativadas nas configurações, a localização, a categoria e a disponibilidade já vêm preenchidas.
- **Edição:** quando recebe um `id` (`EXTRA_ID`). A ferramenta é carregada com `buscarPorId()` e o título da tela muda para "Edição de Ferramenta".
- **Salvar:** valida os campos obrigatórios e então chama `inserir()` para um novo cadastro ou `alterar()` para uma edição. Depois devolve `RESULT_OK` com o nome da ferramenta e um indicador de edição.

#### `ConfiguracoesActivity` — configurações

Tela com botão **Up** e ícone **Salvar**. Nela o usuário escolhe:

- a ordenação da lista (ordem de cadastro, nome ou código);
- se o formulário deve sugerir valores em novos cadastros e, nesse caso, quais são a localização, a categoria e a disponibilidade padrão.

As escolhas são lidas e gravadas por meio da classe `Preferencias`.

#### `SobreActivity` — sobre o aplicativo

Tela com botão **Up** que mostra o logotipo da UTFPR, a descrição do aplicativo e os dados de autoria (aluno, curso e e-mail).

### Classes de apoio

#### `FerramentaAdapter` — adapter customizado

Estende `BaseAdapter` e monta cada linha da lista a partir do layout `item_ferramenta.xml`. Ele usa o padrão **ViewHolder** para reaproveitar as views ao rolar a lista. Cada linha mostra o nome, o código e a categoria, a localização, o estado e uma etiqueta colorida: **DISPONÍVEL** em verde ou **EMPRESTADA** em vermelho. O `getItemId()` devolve o `id` da ferramenta no banco.

#### `Preferencias` — SharedPreferences

Classe utilitária com métodos estáticos que leem e gravam as configurações no arquivo de preferências do app.

| Chave | Tipo | Uso |
|---|---|---|
| `ordenacao` | `int` | `ORDENACAO_CADASTRO`, `ORDENACAO_NOME` ou `ORDENACAO_CODIGO` |
| `sugestoes_ativas` | `boolean` | Ativa o preenchimento automático no cadastro |
| `localizacao_padrao` | `String` | Localização sugerida |
| `categoria_padrao` | `int` | Categoria sugerida (posição no array) |
| `disponivel_padrao` | `boolean` | Disponibilidade sugerida |

## Fluxo de funcionamento

```
ListaFerramentasActivity ──(Adicionar)──────────────▶ MainActivity ──inserir()──▶ Room
        │   ▲                                               │
        │   └──────────── RESULT_OK + onResume recarrega ◀──┘
        │
        ├──(toque longo ▶ Editar, envia id)──────────▶ MainActivity ──alterar()──▶ Room
        ├──(toque longo ▶ Excluir ▶ AlertDialog "Sim")─────────────────excluir()──▶ Room
        ├──(Configurações)───────────────────────────▶ ConfiguracoesActivity ──▶ SharedPreferences
        └──(Sobre)───────────────────────────────────▶ SobreActivity
```

Todas as telas secundárias declaram `ListaFerramentasActivity` como `parentActivityName` no `AndroidManifest.xml`. Por isso o botão **Up** sempre leva de volta à lista.

## Idiomas

| Pasta | Idioma |
|---|---|
| `res/values/strings.xml` | Inglês (padrão) |
| `res/values-pt-rBR/strings.xml` | Português do Brasil |

O Android escolhe os textos conforme o idioma configurado no aparelho. Os arrays de categorias e estados (`res/values/arrays.xml`) apontam para esses textos, então também aparecem traduzidos.

## Tecnologias

- Java 11
- Android SDK: `minSdk` 24, `targetSdk` 36
- AndroidX AppCompat e Material Components
- Room 2.7.2 (`room-runtime` com `annotationProcessor` para o `room-compiler`)

## Como executar

1. Abra o projeto no Android Studio.
2. Aguarde a sincronização do Gradle.
3. Execute a configuração `app` em um emulador ou aparelho com Android 7.0 (API 24) ou superior.

Pela linha de comando:

```bash
./gradlew :app:assembleDebug
```
