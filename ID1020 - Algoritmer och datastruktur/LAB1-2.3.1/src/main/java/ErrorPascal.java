abstract class ErrorPascal implements Pascal {
    // Overloading (?)
    // Simpel sanityCheck, inget viktigt, mer "proof" hur det funkar
    public boolean sanityCheck(int n) {
        return (n > 0);
    }

    public boolean sanityCheck(int n, int k) {
        return (n >= 0 && k >= 0 && n >= k);
    }
}