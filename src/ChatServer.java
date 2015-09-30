import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.LinkedHashSet;

/**
 * A multithreaded chat room server. When a client connects the server requests
 * a screen name by sending the client the text "SUBMITNAME", and keeps
 * requesting a name until a unique one is received. After a client submits a
 * unique name, the server acknowledges with "NAMEACCEPTED". Then all messages
 * from that client will be broadcast to all other clients that have submitted a
 * unique screen name. The broadcast messages are prefixed with "MESSAGE ".
 * 
 */
public class ChatServer {

	/**
	 * The port that the server listens on.
	 */
	private static final int PORT_NUMBER = 9001;
	int bytesRead;
	int current = 0;
	FileOutputStream fos = null;

	/**
	 * The set of all names of clients in the chat room. Maintained so that we
	 * can check that new clients are not registering name already in use.
	 */
	private static LinkedHashSet<String> names = new LinkedHashSet<String>();

	/**
	 * The set of all the print writers for all the clients. This set is kept so
	 * we can easily broadcast messages.
	 */
	private static HashSet<PrintWriter> writers = new HashSet<PrintWriter>();

	/**
	 * The appplication main method, which just listens on a port and spawns
	 * handler threads.
	 */
	public static void main(String[] args) throws Exception {
		System.out.println("The chat server is running.");
		ServerSocket listener = new ServerSocket(PORT_NUMBER);
		try {
			while (true) {
				new Handler(listener.accept()).start();
			}
		} finally {
			listener.close();
		}
	}

	/**
	 * A handler thread class. Handlers are spawned from the listening loop and
	 * are responsible for a dealing with a single client and broadcasting its
	 * messages.
	 */
	private static class Handler extends Thread {
		private String name;
		private Socket socket;
		private BufferedReader in;
		private PrintWriter out;

		/**
		 * Constructs a handler thread, squirreling away the socket. All the
		 * interesting work is done in the run method.
		 */
		public Handler(Socket socket) {
			this.socket = socket;
		}

		/**
		 * Services this thread's client by repeatedly requesting a screen name
		 * until a unique one has been submitted, then acknowledges the name and
		 * registers the output stream for the client in a global set, then
		 * repeatedly gets inputs and broadcasts them.
		 */
		public void run() {
			try {

				// Create character streams for the socket.
				in = new BufferedReader(new InputStreamReader(
						socket.getInputStream()));
				out = new PrintWriter(socket.getOutputStream(), true);

				while (true) {
					out.println("SUBMITNAME");
					name = in.readLine();
					if (name == null) {
						return;
					}
					synchronized (names) {
						if (!names.contains(name)) {
							names.add(name);
							break;
						}
					}
				}

				// After the name has been chosen add the socket to set of all
				// writers so that all the messages could be broadcasted.
				out.println("NAMEACCEPTED");
				writers.add(out);

				// Accept messages from this client and broadcast them.
				while (true) {
					String input = in.readLine();
					System.out.println("input val"+input);
					if (input == null) {
						return;
					}

					for (PrintWriter writer : writers) {
						writer.println("MESSAGE " + name + ": " + input);
					}
				}

			} catch (IOException e) {
				System.out.println(e);
			} finally {
				// Remove clients name and close its socket and printwriter
				if (name != null) {
					names.remove(name);
				}
				if (out != null) {
					writers.remove(out);
				}
				try {
					socket.close();
				} catch (IOException e) {
				}
			}
		}
	}
}