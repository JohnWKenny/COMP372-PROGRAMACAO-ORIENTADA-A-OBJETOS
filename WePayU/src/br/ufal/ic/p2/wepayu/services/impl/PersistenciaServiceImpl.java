package br.ufal.ic.p2.wepayu.services.impl;

import br.ufal.ic.p2.wepayu.services.PersistenciaService;
import br.ufal.ic.p2.wepayu.models.*;
import java.util.Map;
import java.beans.XMLEncoder;
import java.beans.XMLDecoder;
import java.io.*;

/**
 * Implementação do serviço de persistência no sistema WePayU.
 * 
 * <p>Esta classe implementa todas as operações relacionadas à persistência
 * de dados do sistema, incluindo salvamento, carregamento e gerenciamento
 * do estado do sistema usando arquivos XML.</p>
 * 
 * <p>Funcionalidades implementadas:</p>
 * <ul>
 *   <li>Salvamento de dados do sistema em arquivos XML</li>
 *   <li>Carregamento de dados do sistema a partir de arquivos XML</li>
 *   <li>Limpeza de dados do sistema</li>
 *   <li>Encerramento do sistema com salvamento final</li>
 * </ul>
 * 
 * @author John Wallex
 * @version 1.0
 * @since 2025
 */
public class PersistenciaServiceImpl implements PersistenciaService {
    
    private Map<String, Empregado> empregados;
    private Map<String, MembroSindicato> membrosSindicato;
    private int id;
    
    private static final String FILE_EMPREGADOS = "empregados.xml";
    private static final String FILE_SINDICATO = "sindicato.xml";
    
    public PersistenciaServiceImpl(Map<String, Empregado> empregados, 
                                   Map<String, MembroSindicato> membrosSindicato, 
                                   int id) {
        this.empregados = empregados;
        this.membrosSindicato = membrosSindicato;
        this.id = id;
    }
    
    @Override
    public void salvarSistema() {
        try (XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(FILE_EMPREGADOS)))) {
            encoder.writeObject(empregados);
            encoder.writeObject(id);
        } catch (Exception e) {
            System.err.println("Erro ao salvar sistema: " + e.getMessage());
        }

        try (XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(FILE_SINDICATO)))) {
            encoder.writeObject(membrosSindicato);
        } catch (Exception e) {
            System.err.println("Erro ao salvar membros do sindicato: " + e.getMessage());
        }
    }
    
    @Override
    public void carregarSistema() {
        File emps = new File(FILE_EMPREGADOS);
        if (!emps.exists()) return; // primeira execução, nada salvo

        try (XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(FILE_EMPREGADOS)))) {
            empregados.putAll((Map<String, Empregado>) decoder.readObject());
            id = (Integer) decoder.readObject();
        } catch (Exception e) {
            System.err.println("Erro ao carregar sistema: " + e.getMessage());
        }

        File sin = new File(FILE_SINDICATO);
        if (!sin.exists()) return;

        try (XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(FILE_SINDICATO)))) {
            membrosSindicato.putAll((Map<String, MembroSindicato>) decoder.readObject());
        } catch (Exception e) {
            System.err.println("Erro ao carregar membros do sindicato: " + e.getMessage());
        }
    }
    
    @Override
    public void zerarSistema() {
        empregados.clear();
        membrosSindicato.clear();
        id = 0;
    }

    @Override
    public void encerrarSistema() {
        salvarSistema();
    }
}
