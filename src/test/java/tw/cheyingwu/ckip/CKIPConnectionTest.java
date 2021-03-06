package tw.cheyingwu.ckip;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

import tw.cheyingwu.ckip.util.CKIPMessageHelper;

public class CKIPConnectionTest {
	
	private String username;
	private String password;
	private String ip;
	private Integer port;
	
	private String expectXML1 = "<?xml version=\"1.0\" ?><wordsegmentation version=\"0.1\"><processstatus code=\"0\">Success</processstatus><result><sentence>　台新(N)　金控(N)　12月(N)　3日(N)　將(ADV)　召開(Vt)　股東(N)　臨時會(N)　進行(Vt)　董監(N)　改選(Nv)　。(PERIODCATEGORY)</sentence></result></wordsegmentation>";
	private String expectXML2 = "<?xml version=\"1.0\" ?><wordsegmentation version=\"0.1\"><processstatus code=\"0\">Success</processstatus><result><sentence>　中華隊(N)　的(T)　未來(N)　還是(ADV)　充滿(Vt)　了(ASP)　希望(Vt)　。(PERIODCATEGORY)</sentence></result></wordsegmentation>";
	
	private CKIPConnection ckipConn;
	
	@Before
	public void init(){
		try {
			Properties p = new Properties();
			p.load(new FileReader(new File("properties/ckip.properties")));
			
			username = p.getProperty("ckipConnection.username");
			password = p.getProperty("ckipConnection.password");
			ip = p.getProperty("ckipConnection.ip");
			port = Integer.parseInt(p.getProperty("ckipConnection.port"));
			
			ckipConn = new CKIPConnection(ip, port);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testSend() {
		ckipConn.open();
		String actualXML1 = ckipConn.send(CKIPMessageHelper.warp(username, password, "台新金控12月3日將召開股東臨時會進行董監改選。"));
		ckipConn.close();
		
		assertEquals(expectXML1, actualXML1);
	}
	
	@Test
	public void testSendTwice() {
		ckipConn.open();
		String actualXML1 = ckipConn.send(CKIPMessageHelper.warp(username, password, "台新金控12月3日將召開股東臨時會進行董監改選。"));
		String actualXML2 = ckipConn.send(CKIPMessageHelper.warp(username, password, "中華隊的未來還是充滿了希望。"));
		ckipConn.close();
		
		assertEquals(expectXML1, actualXML1);
		assertEquals(expectXML2, actualXML2);
	}

}
