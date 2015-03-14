/*On my honor, I have neither given nor received unauthorized aid on this assignment
Name:- Rohit Garg
UFID:- 17622194
 */

import java.io.*;
import java.math.BigInteger;
import java.util.*;

public class SIM{
	final ArrayList<String> dictionary = new ArrayList<String>();
	final ArrayList<String> original = new ArrayList<String>();
	PrintWriter writer= null;

	private StringBuilder initDecompress(){
		BufferedReader f=null;
		StringBuilder b = new StringBuilder();
		int flag=0;
		try{
			f=new BufferedReader(new FileReader("compressed.txt"));
			String line = null;
			StringTokenizer st =null;
			while((line=f.readLine()) != null){
				st = new StringTokenizer(line);
				String in= st.nextToken();
				if(in.equals("xxxx")){flag =1; continue;}
				if(flag==0){b.append(in);}
				else{dictionary.add(in);}
			}
			f.close();
		}
		catch(FileNotFoundException e){
			e.printStackTrace();
		}
		catch(IOException e){
			e.printStackTrace();
		}
		return b;
	}



	private void initDictionary(){
		Map<String,Integer> frequency = new LinkedHashMap<String,Integer>();
		BufferedReader f;
		try{
			f=new BufferedReader(new FileReader("original.txt"));
			String line = null;
			StringTokenizer st =null;
			while((line=f.readLine()) != null){
				st = new StringTokenizer(line);
				String in= st.nextToken();
				original.add(in);
				if(frequency.containsKey(in)){
					frequency.put(in,frequency.get(in)+1);
				}
				else {
					frequency.put(in,1);
				}			
			}
			f.close();
		}
		catch(FileNotFoundException e){
			e.printStackTrace();
		}
		catch(IOException e){
			e.printStackTrace();
		}
		List<Map.Entry<String, Integer>> entries =
				new ArrayList<Map.Entry<String, Integer>>(frequency.entrySet());
		Collections.sort(entries, new Comparator<Map.Entry<String, Integer>>() {
			public int compare(Map.Entry<String, Integer> a, Map.Entry<String, Integer> b){
				return -a.getValue().compareTo(b.getValue());
			}
		});
		int k=0;
		for (Map.Entry<String, Integer> entry : entries) {
			if(k<8){
				dictionary.add((String)entry.getKey());
			}
			k++;
		}

	}	

	public SIM(){}

	public static void main(String[] args) throws FileNotFoundException{
		SIM m = new SIM();
		int operator;
		if(args.length==1){
			operator= Integer.parseInt(args[0]);
			if(operator==1){
				m.writer= new PrintWriter("cout.txt");
				m.initDictionary();
				m.compress();
				m.writer.close();
			}
			else if(operator==2){
				m.writer= new PrintWriter("dout.txt");
				m.decompress(m.initDecompress().toString());
				m.writer.close();
			}
			else{
				System.exit(1);
			}

		}
		else {
			System.exit(1);
		}



	}


	private String getMin(String a,String b,String c,String d){
		int l1,l2,l3,l4,min;
		if(a==null){l1=Integer.MAX_VALUE;} else{l1=a.length();}
		if(b==null){l2=Integer.MAX_VALUE;} else{l2=b.length();}
		if(c==null){l3=Integer.MAX_VALUE;} else{l3=c.length();}
		if(d==null){l4=Integer.MAX_VALUE;} else{l4=d.length();}
		min=Math.min( Math.min(l1,l2), Math.min(l3,l4));
		if(min==l1){return a;} if(min==l2){return b;} if(min==l3){return c;} if(min==l4){return d;}
		else {return null;}

	}

	private void printFile(StringBuilder build){
		String m=build.toString();
		int len = m.length(),it=len/32,start=0;
		for(int i=0;i<it;i++){
			writer.println(m.substring(start, start+32));
			start+=32;
		}
	}
	
	private void decompress(String in) {
		StringBuilder build = new StringBuilder();
		int len=in.length();
		String op =null;
		for(int i=0;i<len;){
			if(i+2<len)	op = in.substring(i, i+2);
			else break;
			int type= Integer.parseInt(op, 2);
			switch(type){
				case 0:
					if(i+5<len){
						String ins = in.substring(i+2, i+5);
						int index = Integer.parseInt(ins, 2);
						build.append(dictionary.get(index));
						i=i+5;
					}
					else{i++;}
					break;
				case 1:
					if(i+10<len){
						String mis = in.substring(i+2,i+7);
						String ins2= in.substring(i+7,i+10);
						int index1 = Integer.parseInt(mis, 2);
						int index2 = Integer.parseInt(ins2, 2);
						String d = dictionary.get(index2);
						if(d.charAt(index1)=='0'){d=d.substring(0, index1)+"11"+d.substring(index1+2);}
						else{d=d.substring(0, index1)+"00"+d.substring(index1+2);}
						build.append(d);
						i=i+10;
					}
					else{i++;}
					break;
				case 2:
					if(i+15<len){
						String mis1= in.substring(i+2,i+7);
						String mis2= in.substring(i+7,i+12);
						String ins3= in.substring(i+12,i+15);
						int index11 = Integer.parseInt(mis1, 2);
						int index12 = Integer.parseInt(mis2, 2);
						int index13 = Integer.parseInt(ins3, 2);
						String t= dictionary.get(index13);
						if(t.charAt(index11)=='0'){t=t.substring(0, index11)+"1"+t.substring(index11+1);}
						else{t=t.substring(0, index11)+"0"+t.substring(index11+1);}
						if(t.charAt(index12)=='0'){t=t.substring(0, index12)+"1"+t.substring(index12+1);}
						else{t=t.substring(0, index12)+"0"+t.substring(index12+1);}
						build.append(t);
						i=i+15;
					}
					else{i++;}
					break;
				case 3:
					if(i+34<len){
						build.append(in.substring(i+2,i+34));
						i=i+34;
					}
					else{i++;}
					break;
			}
		}
		printFile(build);
	}
	private void compress() {
		String out=null;
		String case1=null;
		String case2=null;
		String case3=null;
		String case4=null;	
		StringBuilder build = new StringBuilder();
		for(int i=0;i<original.size();i++){
			out=case1=case2=case3=case4=null;
			for(int j=0;j<dictionary.size();j++){
				long num1=new BigInteger(original.get(i), 2).longValue();
				long num2=new BigInteger(dictionary.get(j), 2).longValue();

				if(num1>num2){
					num1=num1-num2;
				}
				else{
					num1=num2-num1;
				}

				String comp= String.format("%032d", new BigInteger(Long.toBinaryString(num1)));
				int a= comp.indexOf("1");
				int b= comp.indexOf("1", a+1);
				int c= comp.indexOf("1", b+1);

				if(a==-1){
					case1 = "00"+String.format("%03d", new BigInteger(Integer.toBinaryString(j)));
				}

				else if ((a!=-1 && b!=-1 && c==-1) && (b==a+1)){
					case2= "01"+String.format("%05d", new BigInteger(Integer.toBinaryString(a)))+String.format("%03d", new BigInteger(Integer.toBinaryString(j)));
				}

				else if((a!=-1 && b!=-1 && c==-1) && (b!=a+1)){
					case3= "10"+String.format("%05d", new BigInteger(Integer.toBinaryString(a)))+String.format("%05d", new BigInteger(Integer.toBinaryString(b)))+String.format("%03d", new BigInteger(Integer.toBinaryString(j)));
				}

				else if((a!=-1 && b!=-1 && c!=-1) || (a!=-1 && b==-1)){
					case4 = "11"+original.get(i);	
				}
			}
			String min=getMin(case1, case2, case3, case4);
			if(min!=null){out=min;}
			build.append(out);
		}
		int rem = build.toString().length()%32;
		for(int i=0;i<32-rem;i++){build.append("1");}
		printFile(build);
		writer.println("xxxx");
		for(int i=0;i<dictionary.size();i++){writer.println(dictionary.get(i));}
	}

}
