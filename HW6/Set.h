#ifndef SET_H_
#define SET_H_
#include <iostream>
using namespace std;

class Set {

	public:
		Set(int l);
		Set(const Set&);
		~Set();

		int getCopyCount() const;

		Set& operator+(const int);
		Set& operator-(const int);
		Set operator&(Set&);
		Set operator~();
		Set operator/(Set&);

		friend ostream& operator<<(ostream& os, const Set&);

	private:
		bool* slots;
		int l;
		int copyCount;

};



#endif /* SET_H_ */
