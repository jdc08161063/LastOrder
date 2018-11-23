package utility;

import java.io.*;
import java.util.*;

public class WindowsCommandTools
{
	public static void DeleteDirectory(String dir) 
	{
		try
		{
			//FileUtils.deleteDirectory(new File(dir));
			WindowsCommandTools.RunWindowsCommand("rmdir /S /Q " + dir, true, false);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void printMessage(final InputStream input) {
		new Thread(new Runnable() {
			public void run() {
				Reader reader = new InputStreamReader(input);
				BufferedReader bf = new BufferedReader(reader);
				StringBuilder output = new StringBuilder();
				String line;
				try {
					while((line=bf.readLine())!=null) {
						output.append(line);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	public static void RunWindowsExeLocal(String dir, String exe, boolean waitFor, boolean exitIfException)
	{
		try
		{
			String windowsCommandPrefix = "CMD /C ";
			String completeCommand = windowsCommandPrefix + "cd /D " + dir + " & " + exe;
			System.out.println(completeCommand);
			System.out.println("C:\\Python36\\python C:\\StarcraftAITournamentManager-master\\client\\start_starcraft.py");
			//Process proc = Runtime.getRuntime().exec(completeCommand);

			//Process proc = Runtime.getRuntime().exec("C:\\Python36\\python C:\\StarcraftAITournamentManager-master\\client\\start_starcraft.py");
			try {
				//Process proc = Runtime.getRuntime().exec("C:\\Python36\\python C:\\StarcraftAITournamentManager-master\\client\\start_starcraft.py");
				//printMessage(proc.getInputStream());
				//printMessage(proc.getErrorStream());
				//int value = proc.waitFor();

				//ProcessBuilder probuilder = new ProcessBuilder("C:\\Python36\\python", "C:\\StarcraftAITournamentManager-master\\client\\start_starcraft.py");
				//ProcessBuilder probuilder = new ProcessBuilder(completeCommand);
				//Process proc = probuilder.start();

				Process proc = Runtime.getRuntime().exec("C:\\Python36\\python C:\\StarcraftAITournamentManager-master\\client\\start_starcraft.py");
				printMessage(proc.getErrorStream());
				printMessage(proc.getInputStream());
				proc.waitFor();

				//int exitVal = proc.waitFor();
				//System.out.println("\n\nExit Value is " + exitVal);
			} catch (Exception e) {
				e.printStackTrace();
			}

			//if (waitFor)
			//{
			//	proc.waitFor();
			//}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			
			if (exitIfException)
			{
				System.exit(-1);
			}
		}
	}
	
	public static void KillWindowsProcessID(int pid)
	{
		WindowsCommandTools.RunWindowsCommand("taskkill /F /PID " + pid, true, false);
	}
	
	public static void KillExcessWindowsProccess(Vector<Integer> doNotKill) 
	{
		if (doNotKill == null)
		{
			return;
		}
	
		Vector<Integer> running = GetRunningProcesses();
		for (int i = 0; i < running.size(); i++) 
		{
			if (!(doNotKill.contains(running.get(i)))) 
			{
				KillWindowsProcessID(running.get(i));
			}
		}
	}
	
	public static Vector<Integer> GetRunningProcesses() 
	{
		Vector<Integer> running = new Vector<Integer>();
		Process p;
		try 
		{
			p = Runtime.getRuntime().exec("tasklist.exe /FO LIST");

			BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line;
			
			String[] splitout;
			while ((line = stdInput.readLine()) != null) 
			{
				if (line.startsWith("PID:")) 
				{
					splitout = line.split(":");
					if (splitout.length > 1) 
					{
						running.add(Integer.parseInt(splitout[1].trim()));
					}
				}
			}
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}

		return running;
	}
	
	public static boolean IsWindowsProcessRunning(String process) 
	{
		try 
		{
			Process p = Runtime.getRuntime().exec("tasklist.exe");
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));

			String line;
			while ((line = stdInput.readLine()) != null) 
			{
				if (line.contains(process)) 
				{
					return true;
				}
			}
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		return false;
	}
	
	public static void RunWindowsCommandAsync(String command)
	{
		try
		{
			String windowsCommandPrefix = "CMD /C ";
			String completeCommand = windowsCommandPrefix + command;
			System.out.println("\n" + completeCommand);
			Runtime.getRuntime().exec(command);
		}
		catch (Exception e)
		{
			
		}
	}
	
	public static void RunWindowsCommand(String command, boolean waitFor, boolean exitIfException)
	{
		try
		{
			String windowsCommandPrefix = "CMD /C ";
			String completeCommand = windowsCommandPrefix + command;
			System.out.println("\n" + completeCommand);
			Process proc = Runtime.getRuntime().exec(windowsCommandPrefix + command);

			printMessage(proc.getInputStream());
			printMessage(proc.getErrorStream());
			/*
			if (command.indexOf("run_proxy.bat") != -1)
			{
				BufferedReader stdInput = new BufferedReader(new
						InputStreamReader(proc.getInputStream()));

				BufferedReader stdError = new BufferedReader(new
						InputStreamReader(proc.getErrorStream()));

				// read the output from the command
				System.out.println("Here is the standard output of the command:\n");
				String s = null;
				while ((s = stdInput.readLine()) != null) {
					System.out.println(s);
				}
				// read any errors from the attempted command
				System.out.println("Here is the standard error of the command (if any):\n");
				while ((s = stdError.readLine()) != null) {
					System.out.println(s);
				}
			}
			*/

			if (waitFor)
			{
				proc.waitFor();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.err.println("\n\nCommandTools: Could not run command:\n" + command + "\n\n");
			
			if (exitIfException)
			{
				System.exit(-1);
			}
		}
	}
	
	public static void RunWindowsCommandAsAdmin(String command, boolean waitFor, boolean exitIfException)
	{
		try
		{
			String windowsCommandPrefix = "CMD /C runas /user:Administrator ";
			String completeCommand = windowsCommandPrefix + command;
			System.out.println("\n" + completeCommand);
			Process proc = Runtime.getRuntime().exec(windowsCommandPrefix + command);
						
			if (waitFor)
			{
				proc.waitFor();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.err.println("\n\nCommandTools: Could not run command:\n" + command + "\n\n");
			
			if (exitIfException)
			{
				System.exit(-1);
			}
		}
	}
	
	public static void RegEdit(String keyName, String valueName, String type, String data)
	{
		String q = "\"";
	
		// make sure there are no quotations in the input strings
		keyName.replaceAll(q, "");
		valueName.replaceAll(q, "");
		type.replaceAll(q, "");
		data.replaceAll(q, "");
		
		// wrap quotations around the values to be sure
		keyName = q + keyName + q;
		valueName = q + valueName + q;
		data = q + data + q;
		
		String cmd = "reg add " + keyName + " /f /v " + valueName + " /t " + type + " /d " + data;
		RunWindowsCommand(cmd, true, false);
		
		System.out.println(cmd);
	}
	
	public static void main(String[] args)
	{
		RunWindowsCommandAsAdmin("notepad", true, false);	
	}
}