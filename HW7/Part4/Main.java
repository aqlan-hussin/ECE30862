import java.io.*;

class Main {

   public static void main(String args[]) {
      char c = 0;
      short s = 1;
      int i = 2;
      float f = 0.0f;
      double df = 1.0;

      B b = new B( );
      D d = new D( );

      System.out.println("Direct calls");
      //d.foo(d, f); 
      //d.foo(c, d, df);
      d.foo(df); // prints "B:foo(double)"
      d.foo(i);  // prints "D:foo(float)"
      //d.foo( );
      //d.foo(i, d, df);
      d.foo(c, b, f); // prints "D:foo(int, B, float)"
      d.foo(f); // prints "D:foo(float)"
      d.foo(c); // prints "D:foo(float)"
      d.foo(s, d, df); // prints "B:foo(short, B, double)"
      //d.foo(i, d);

      System.out.println("b.caller");
      b.caller( ); // prints "B:foo(short) B:foo(double)"

   }
}
