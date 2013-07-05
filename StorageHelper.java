public class StorageHelper {

	private Context context;
	private SharedPreferences sharedPreferences;
	private String PREFS_NAME = "StorageHelper";
	private SharedPreferences.Editor sharedEditor;

	// Create object StorageHelper with Activity context
	public StorageHelper(Context context) {
		this.context = context;
		sharedPreferences = context.getSharedPreferences(PREFS_NAME, 0);
		sharedEditor = sharedPreferences.edit();
	}

	// Save string text on a file in internat storage
	public void saveInternalFile(String FILENAME, String value)
			throws IOException {
		FileOutputStream fos = context.openFileOutput(FILENAME,
				Context.MODE_PRIVATE);
		fos.write(value.getBytes());
		fos.close();
	}

	// Get file on internal storage
	public String getInternalFile(String FILENAME) {

		FileInputStream in;
		try {
			in = context.openFileInput(FILENAME);
			InputStreamReader inputStreamReader = new InputStreamReader(in);
			BufferedReader bufferedReader = new BufferedReader(
					inputStreamReader);
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				sb.append(line);
			}

			return sb.toString();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	// Save file on external storage
	public void saveExternalStorage(String FILENAME, String value) {
		// /Android/data/<package_name>/files/

		boolean mExternalStorageAvailable = false;
		boolean mExternalStorageWriteable = false;
		String state = Environment.getExternalStorageState();

		if (Environment.MEDIA_MOUNTED.equals(state)) {
			mExternalStorageAvailable = mExternalStorageWriteable = true;
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			mExternalStorageAvailable = true;
			mExternalStorageWriteable = false;
		} else {
			mExternalStorageAvailable = mExternalStorageWriteable = false;
		}
	}

	// Return String data from Raw file by resource ID
	public String getFileRaw(int resourceID) {
		InputStream raw = context.getResources().openRawResource(resourceID);
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		int i;
		try {
			i = raw.read();
			while (i != -1) {
				stream.write(i);
				i = raw.read();
			}
			raw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return stream.toString();
	}

	// Return byte[] data from Raw file
	public byte[] getBytesRaw(int resourceID) {
		InputStream raw = context.getResources().openRawResource(resourceID);
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		int i;
		try {
			i = raw.read();
			while (i != -1) {
				stream.write(i);
				i = raw.read();
			}
			raw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return stream.toByteArray();
	}

	// Return path external Storage
	public String pathExternalDirectory() {
		return Environment.getExternalStorageDirectory().toString();
	}

	// Return path internal Storage
	public String pathInternalDirectory() {
		return context.getApplicationContext().getFilesDir().getAbsolutePath();
	}

	// Return all files who start by prefix in directory
	public ArrayList<File> fileListFromDirectory(String directory, String prefix) {
		ArrayList<File> files = new ArrayList<File>();

		File dir = new File(directory);
		for (File child : dir.listFiles()) {
			if (child.getName().startsWith(prefix)) {
				files.add(child);
			}
		}
		return files;
	}

	public String convertStreamToString(File file) {
		try {
			FileInputStream fis = new FileInputStream(file);
			return convertStreamToString(fis);
		} catch (Exception e) {
			return "";
		}
	}

	public String convertStreamToString(InputStream is) throws Exception {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) {
			sb.append(line).append("\n");
		}
		return sb.toString();
	}

	public String getStringFromFile(String filePath) throws Exception {
		File fl = new File(filePath);
		FileInputStream fin = new FileInputStream(fl);
		String ret = convertStreamToString(fin);
		// Make sure you close all streams.
		fin.close();
		return ret;
	}

	public String downloadHTML(String url) throws ClientProtocolException,
			IOException {
		HttpClient httpClient = new DefaultHttpClient();
		HttpContext localContext = new BasicHttpContext();
		HttpGet httpGet = new HttpGet(url);
		HttpResponse response = httpClient.execute(httpGet, localContext);
		String result = "";

		BufferedReader reader = new BufferedReader(new InputStreamReader(
				response.getEntity().getContent()));

		String line = null;
		while ((line = reader.readLine()) != null) {
			result += line + "\n";
		}
		return result;
	}

	public static String createUniqueReference() {
		long time = System.currentTimeMillis();
		return Long.toString(time);
	}

	/**
	 * Remove file from internal Storage
	 * 
	 * @param FILENAME
	 * @return
	 */
	public boolean removeInternalStorage(String FILENAME) {
		File dir = context.getFilesDir();
		File file = new File(dir, FILENAME);
		return file.delete();
	}
}
