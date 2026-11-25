import java.util.Scanner;

public class Streaming {
    static final int MAX = 100;
    static Scanner teclado = new Scanner(System.in);

    static int[] arvCodigo = new int[MAX];
    static String[] arvTitulo = new String[MAX];
    static int[] arvEsq = new int[MAX];
    static int[] arvDir = new int[MAX];
    static int arvTotal = 0;
    static int raiz = -1;

    static int[] pilha = new int[MAX];
    static int topo = 0;

    static int[] fila = new int[MAX];
    static int filaInicio = 0;
    static int filaTotal = 0;

    public static void main(String[] args) {
        inserirArvore(50, "Marvel Dooms Days");
        inserirArvore(30, "Kimetsu no Yaiba, Castelo infinito");
        inserirArvore(70, "Cidade de Deus");
        inserirArvore(20, "MIB homens de preto");
        inserirArvore(40, "Naruto The last");
        int opcao;
        do {
            System.out.println("\n1-Cadastrar 2-Buscar 3-Remover 4-Percursos");
            System.out.println("5-Assistir 6-Ver histórico 7-Voltar (desfazer)");
            System.out.println("8-Assistir depois 9-Ver lista 10-Assistir próxima 0-Sair");
            opcao = numero("Opção: ");
            if (opcao == 1) {
                cadastrar();
            } else if (opcao == 2) {
                buscar();
            } else if (opcao == 3) {
                remover();
            } else if (opcao == 4) {
                percursos();
            } else if (opcao == 5) {
                assistir();
            } else if (opcao == 6) {
                listarPilha();
            } else if (opcao == 7) {
                voltar();
            } else if (opcao == 8) {
                guardar();
            } else if (opcao == 9) {
                listarFila();
            } else if (opcao == 10) {
                assistirProxima();
            }
        } while (opcao != 0);
    }

    static boolean inserirArvore(int codigo, String titulo) {
        if (buscarArvore(codigo) != -1 || arvTotal == MAX) {
            return false;
        }
        int novo = arvTotal;
        arvTotal++;
        arvCodigo[novo] = codigo;
        arvTitulo[novo] = titulo;
        arvEsq[novo] = -1;
        arvDir[novo] = -1;
        if (raiz == -1) {
            raiz = novo;
            return true;
        }
        int atual = raiz;
        while (true) {
            if (codigo < arvCodigo[atual]) {
                if (arvEsq[atual] == -1) {
                    arvEsq[atual] = novo;
                    return true;
                }
                atual = arvEsq[atual];
            } else {
                if (arvDir[atual] == -1) {
                    arvDir[atual] = novo;
                    return true;
                }
                atual = arvDir[atual];
            }
        }
    }

    static int buscarArvore(int codigo) {
        int atual = raiz;
        while (atual != -1 && arvCodigo[atual] != codigo) {
            if (codigo < arvCodigo[atual]) {
                atual = arvEsq[atual];
            } else {
                atual = arvDir[atual];
            }
        }
        return atual;
    }

    static int removerNo(int no, int codigo) {
        if (no == -1) {
            return -1;
        }
        if (codigo < arvCodigo[no]) {
            arvEsq[no] = removerNo(arvEsq[no], codigo);
        } else if (codigo > arvCodigo[no]) {
            arvDir[no] = removerNo(arvDir[no], codigo);
        } else if (arvEsq[no] == -1) {
            return arvDir[no];
        } else if (arvDir[no] == -1) {
            return arvEsq[no];
        } else {
            int menor = arvDir[no];
            while (arvEsq[menor] != -1) {
                menor = arvEsq[menor];
            }
            arvCodigo[no] = arvCodigo[menor];
            arvTitulo[no] = arvTitulo[menor];
            arvDir[no] = removerNo(arvDir[no], arvCodigo[menor]);
        }
        return no;
    }

    static void emOrdem(int no) {
        if (no != -1) {
            emOrdem(arvEsq[no]);
            System.out.print(arvCodigo[no] + "-" + arvTitulo[no] + " | ");
            emOrdem(arvDir[no]);
        }
    }

    static void preOrdem(int no) {
        if (no != -1) {
            System.out.print(arvCodigo[no] + "-" + arvTitulo[no] + " | ");
            preOrdem(arvEsq[no]);
            preOrdem(arvDir[no]);
        }
    }

    static void posOrdem(int no) {
        if (no != -1) {
            posOrdem(arvEsq[no]);
            posOrdem(arvDir[no]);
            System.out.print(arvCodigo[no] + "-" + arvTitulo[no] + " | ");
        }
    }

    static void empilhar(int codigo) {
        if (topo < MAX) {
            pilha[topo] = codigo;
            topo++;
        }
    }

    static boolean buscarPilha(int codigo) {
        for (int i = 0; i < topo; i++) {
            if (pilha[i] == codigo) {
                return true;
            }
        }
        return false;
    }

    static int desempilhar() {
        if (topo == 0) {
            return -1;
        }
        topo--;
        return pilha[topo];
    }

    static void listarPilha() {
        if (topo == 0) {
            System.out.println("Histórico vazio.");
        }
        for (int i = topo - 1; i >= 0; i--) {
            mostrarObra(pilha[i]);
        }
    }

    static void enfileirar(int codigo) {
        if (filaTotal < MAX) {
            fila[(filaInicio + filaTotal) % MAX] = codigo;
            filaTotal++;
        }
    }

    static boolean buscarFila(int codigo) {
        for (int i = 0; i < filaTotal; i++) {
            if (fila[(filaInicio + i) % MAX] == codigo) {
                return true;
            }
        }
        return false;
    }

    static int desenfileirar() {
        if (filaTotal == 0) {
            return -1;
        }
        int codigo = fila[filaInicio];
        filaInicio = (filaInicio + 1) % MAX;
        filaTotal--;
        return codigo;
    }

    static void listarFila() {
        if (filaTotal == 0) {
            System.out.println("Lista vazia.");
        }
        for (int i = 0; i < filaTotal; i++) {
            mostrarObra(fila[(filaInicio + i) % MAX]);
        }
    }

    static void mostrarObra(int codigo) {
        int pos = buscarArvore(codigo);
        if (pos == -1) {
            System.out.println(codigo + " (removida do acervo)");
        } else {
            System.out.println(arvCodigo[pos] + " - " + arvTitulo[pos]);
        }
    }

    static void cadastrar() {
        int codigo = numero("Código: ");
        System.out.print("Título: ");
        String titulo = teclado.nextLine();
        System.out.println(inserirArvore(codigo, titulo) ? "Cadastrada." : "Código já existe.");
    }

    static void buscar() {
        int pos = buscarArvore(numero("Código: "));
        if (pos == -1) {
            System.out.println("Não encontrada.");
        } else {
            System.out.println("Achei: " + arvCodigo[pos] + " - " + arvTitulo[pos]);
        }
    }

    static void remover() {
        int codigo = numero("Código: ");
        if (buscarArvore(codigo) == -1) {
            System.out.println("Não encontrada.");
        } else {
            raiz = removerNo(raiz, codigo);
            System.out.println("Removida.");
        }
    }

    static void percursos() {
        System.out.print("In-ordem:  ");
        emOrdem(raiz);
        System.out.print("\nPré-ordem: ");
        preOrdem(raiz);
        System.out.print("\nPós-ordem: ");
        posOrdem(raiz);
        System.out.println();
    }

    static void assistir() {
        int codigo = numero("Código: ");
        if (buscarArvore(codigo) == -1) {
            System.out.println("Não encontrada.");
            return;
        }
        if (buscarPilha(codigo)) {
            System.out.println("(você já assistiu essa antes)");
        }
        empilhar(codigo);
        System.out.println("Assistindo: ");
        mostrarObra(codigo);
    }

    static void voltar() {
        int codigo = desempilhar();
        System.out.println(codigo == -1 ? "Histórico vazio." : "Saiu do histórico: " + codigo);
    }

    static void guardar() {
        int codigo = numero("Código: ");
        if (buscarArvore(codigo) == -1) {
            System.out.println("Não encontrada.");
        } else if (buscarFila(codigo)) {
            System.out.println("Já está na lista.");
        } else {
            enfileirar(codigo);
            System.out.println("Guardada na lista.");
        }
    }

    static void assistirProxima() {
        int codigo = desenfileirar();
        if (codigo == -1) {
            System.out.println("Lista vazia.");
        } else if (buscarArvore(codigo) == -1) {
            System.out.println("Essa obra foi removida do acervo.");
        } else {
            empilhar(codigo);
            System.out.println("Assistindo: ");
            mostrarObra(codigo);
        }
    }

    static int numero(String pergunta) {
        while (true) {
            System.out.print(pergunta);
            try {
                return Integer.parseInt(teclado.nextLine().trim());
            } catch (NumberFormatException erro) {
                System.out.println("Digite só números.");
            }
        }
    }
}