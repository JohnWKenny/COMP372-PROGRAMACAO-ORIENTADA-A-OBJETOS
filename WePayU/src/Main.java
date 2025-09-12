import easyaccept.EasyAccept;

/**
 * Classe principal do sistema WePayU.
 * 
 * <p>Esta classe contém o método main que executa os testes automatizados
 * do sistema WePayU usando o framework EasyAccept. Os testes são organizados
 * por casos de uso (us1, us2, etc.) e podem ser executados individualmente
 * descomentando as linhas correspondentes.</p>
 * 
 * <p>Casos de uso disponíveis:</p>
 * <ul>
 *   <li><strong>us1:</strong> Criação de empregados</li>
 *   <li><strong>us2:</strong> Alteração de empregados</li>
 *   <li><strong>us3:</strong> Remoção de empregados</li>
 *   <li><strong>us4:</strong> Lançamento de cartão de ponto</li>
 *   <li><strong>us5:</strong> Lançamento de vendas</li>
 *   <li><strong>us6:</strong> Lançamento de taxas de serviço</li>
 *   <li><strong>us7:</strong> Rodar folha de pagamento</li>
 *   <li><strong>us8:</strong> Undo/Redo</li>
 *   <li><strong>us9:</strong> Agenda de pagamento</li>
 *   <li><strong>us10:</strong> Persistência</li>
 * </ul>
 * 
 * @author John Wallex
 * @version 1.0
 * @since 2025
 */
public class Main {
    /**
     * Método principal que executa os testes automatizados do sistema WePayU.
     * 
     * <p>Este método configura e executa os testes usando o framework EasyAccept.
     * Para executar um teste específico, descomente a linha correspondente
     * ao caso de uso desejado.</p>
     * 
     * <p>Exemplo de uso:</p>
     * <pre>
     * // Para testar criação de empregados:
     * EasyAccept.main(new String[]{facade, "tests/us1.txt"});
     * 
     * // Para testar folha de pagamento:
     * EasyAccept.main(new String[]{facade, "tests/us7.txt"});
     * </pre>
     * 
     * @param args Argumentos da linha de comando (não utilizados)
     */
    public static void main(String[] args) {
        String facade = "br.ufal.ic.p2.wepayu.Facade";

//        EasyAccept.main(new String[]{facade, "tests/us1.txt"});
//        EasyAccept.main(new String[]{facade, "tests/us1_1.txt"});
//        EasyAccept.main(new String[]{facade, "tests/us2.txt"});
//        EasyAccept.main(new String[]{facade, "tests/us2_1.txt"});
//        EasyAccept.main(new String[]{facade, "tests/us3.txt"});
//        EasyAccept.main(new String[]{facade, "tests/us3_1.txt"});
//        EasyAccept.main(new String[]{facade, "tests/us4.txt"});
//        EasyAccept.main(new String[]{facade, "tests/us4_1.txt"});
//        EasyAccept.main(new String[]{facade, "tests/us5.txt"});
//        EasyAccept.main(new String[]{facade, "tests/us5_1.txt"});
//        EasyAccept.main(new String[]{facade, "tests/us6.txt"});
//        EasyAccept.main(new String[]{facade, "tests/us6_1.txt"});
        EasyAccept.main(new String[]{facade, "tests/us7.txt"});
        EasyAccept.main(new String[]{facade, "tests/us8.txt"});


//        EasyAccept.main(new String[]{facade, "tests/us9.txt"});
//        EasyAccept.main(new String[]{facade, "tests/us9_1.txt"});
//        EasyAccept.main(new String[]{facade, "tests/us10.txt"});
//        EasyAccept.main(new String[]{facade, "tests/us10_1.txt"});
    }
}