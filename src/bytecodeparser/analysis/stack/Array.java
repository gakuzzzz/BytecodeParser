/*
 *  Copyright (C) 2011 Stephane Godbillon
 *  
 *  This file is part of BytecodeParser. See the README file in the root
 *  directory of this project.
 *
 *  BytecodeParser is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.

 *  BytecodeParser is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.

 *  You should have received a copy of the GNU Lesser General Public License
 *  along with BytecodeParser.  If not, see <http://www.gnu.org/licenses/>.
 */
package bytecodeparser.analysis.stack;

/**
 * A StackElement that stands for an array.
 * @author Stephane Godbillon
 *
 */
public class Array extends StackElement {
	/**
	 * The signature of this array.
	 */
	public final String signature;
	public Array(String signature) {
		this.signature = signature;
	}
	@Override
	public Array copy() {
		return new Array(signature);
	}
	@Override
	public String toString() {
		return "simple array of '" + signature + "'";
	}
}