public class ArrayOfModularInverses {
	
	static long MOD = 998244353l;

	public static void main(String[] args) 
	{
		int n = 100000;
		
		long[] inv = new long[n];
		inv[1] = 1;
		for (int i=2; i<n; ++i)
			inv[i] = (MOD - (MOD/i) * inv[(int)(MOD%i)] % MOD) % MOD;
		
		//Calculates all the modulo inverses of 1 to n, and stores it in array inv.
		//Complexity is O(n).
	}

}
