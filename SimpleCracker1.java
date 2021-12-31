/**
 * 
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author varunpotluri
 *
 */
public class SimpleCracker1 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SimpleCracker1 simpleCracker = new SimpleCracker1();
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			
			List<String> shadowFileList = simpleCracker.getShadowFileContents();
	         
			InputStream commonPwdsStream = SimpleCracker1.class.getResourceAsStream("/common-passwords.txt");
			 
			BufferedReader commonPwdsReader = new BufferedReader(new InputStreamReader(commonPwdsStream));
	
	  
	        String commonPwdsLine;
	        
	        // outputting each line of the common pwds file.
	        while ((commonPwdsLine = commonPwdsReader.readLine()) != null) {
	                //System.out.println("commonPwdsLine: " +  commonPwdsLine);
	                
        		for(String shadowFileUserPwd : shadowFileList) {
        		
        	        	
        	        String salt = shadowFileUserPwd.split(":")[1];
        	       	String shadowHexPwd = shadowFileUserPwd.split(":")[2];
        	        	
        	       	md.update(salt.getBytes());
        	        	
        	       	byte[] bytes = md.digest(commonPwdsLine.getBytes());
        	        	
        	       	String commonHexPwdToCompare = convertBytesToHexStr(bytes);
        	       	
        	       //	System.out.println("commonPwd : " + commonPwdsLine + " || Salt : " + salt + " || shadowHexPwd : " + shadowHexPwd);
        	       	//System.out.println("commonHexPwdToCompare : " + commonHexPwdToCompare);
        	        	
    	        	if(shadowHexPwd.equals(commonHexPwdToCompare) ) {
    	        		System.out.println("Common Password Matches for user - " + shadowFileUserPwd.split(":")[0] + " : " + commonPwdsLine);
    	        		//System.out.println("Salt : " + salt + " || HexPwd : " + shadowHexPwd);
    	        		//System.out.println("HexPwdGeneratedWithSalt : " + commonHexPwdToCompare +"\n");
    	        	}
        	      }
	        }
	       
	        commonPwdsReader.close();
      
		}catch(Exception e) {
			e.printStackTrace();
		}
	} 
    
	private  List<String> getShadowFileContents() throws IOException{
		List<String> shadowFileList = new ArrayList<>();
		
		InputStream shadowStream = SimpleCracker.class.getResourceAsStream("/shadow-simple");
	  	
		BufferedReader shadowReader = new BufferedReader(new InputStreamReader(shadowStream));
		
		 String shadowLine;
	        
        // outputting each line of the shadow simple file.
        while ((shadowLine = shadowReader.readLine()) != null) {
        	
        	shadowFileList.add(shadowLine);
        }
	    shadowReader.close();
	    
		return shadowFileList;
	}
	
	private static String convertBytesToHexStr(byte[] pwdDigestbytes ) {
		
		BigInteger bigInteger = new BigInteger(pwdDigestbytes);
		
		return String.format("%0" + (pwdDigestbytes.length << 1) + "X", bigInteger);
	}

	
}
