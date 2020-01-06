package br.com.clinic.security;

import javax.ejb.Remote;

@Remote
public interface GeneratorPasswordServiceRemote {

	String gerarNovaSenha();

}
