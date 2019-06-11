package data;

/**
 * Class to implement an integer-tuple.
 * @author tommy
 *
 */
public class Tuple {
	
	public int first;
	public int second;

	/**
	 * Creates a new <code>Tuple</code>.
	 * @param first		first entry in the tuple
	 * @param second	second entry in the tuple
	 */
	public Tuple(int first, int second) {
		this.first  = first;
		this.second = second;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + first;
		result = prime * result + second;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Tuple other = (Tuple) obj;
		if (first != other.first)
			return false;
		if (second != other.second)
			return false;
		return true;
	}
	
	
}
