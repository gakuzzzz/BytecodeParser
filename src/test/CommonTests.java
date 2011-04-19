package test;

import java.util.Arrays;

import bclibs.analysis.Context;
import bclibs.analysis.decoders.DecodedMethodInvocationOp;
import bclibs.analysis.decoders.DecodedMethodInvocationOp.MethodParam;
import bclibs.analysis.stack.StackElement;
import bclibs.analysis.stack.TOP;
import bclibs.analysis.stack.TrackableArray;
import bclibs.analysis.stack.ValueFromLocalVariable;
import bclibs.analysis.stack.StackAnalyzer.Frame;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

public class CommonTests {
	public static CtClass getCtClass(String clazz) {
		ClassPool cp = ClassPool.getDefault();
		try {
			return cp.get(clazz);
		} catch (NotFoundException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static CtMethod getMethod(CtClass ctClass, String name) {
		for(CtMethod ctMethod : ctClass.getMethods()) {
			if(ctMethod.getName().equals(name)) {
				return ctMethod;
			}
		}
		throw new RuntimeException("method '" + name + "' not found in class " + ctClass);
	}
	
	public static void assertDeepEquals(String[] array1, String[] array2) {
		if(array1.length != array2.length)
			throw new RuntimeException(Arrays.toString(array1) + " does not equal " + Arrays.toString(array2) + " !");
		for(int i = 0; i < array1.length; i++)
			if(array1[i] != array2[i] && (array1[i] == null || !array1[i].equals(array2[i])) )
				throw new RuntimeException(Arrays.toString(array1) + " does not equal " + Arrays.toString(array2) + " !");
	}
	
	// old
	
	private static String getLocalVariableName(StackElement se) {
		if(se instanceof ValueFromLocalVariable) {
			ValueFromLocalVariable v = (ValueFromLocalVariable) se;
			if(v.localVariable != null)
				return v.localVariable.name;
		}
		return null;
	}
	
	public static String getMethodNamedSignature(Context context, Frame frame) {
		//System.out.println(frame.op.as(MethodInvocationOpcode.class).decode(context, frame.index).getDescriptor());
		DecodedMethodInvocationOp decoded = (DecodedMethodInvocationOp) frame.decodedOp;
		String name = decoded.getName();
		MethodParam[] params = DecodedMethodInvocationOp.resolveParameters(frame);
		String[] names = new String[params.length];
		for(int i = 0; i < params.length; i++) {
			MethodParam param = params[i];
			names[i] = param.name;
		}
		//String[] names = //methodInvocationNames(frame);
		StringBuffer sb = new StringBuffer();
		if(names.length > 0) {
			sb.append(names[0]);
			for(int i = 1; i < names.length; i++) {
				sb.append(", ").append(names[i]);
			}
		}
		sb.insert(0, "(").insert(0, name).append(")");
		return sb.toString();
	}
	
	public static String[] methodInvocationNames(Frame frame) {
		DecodedMethodInvocationOp decoded = (DecodedMethodInvocationOp) frame.decodedOp;
		String name = decoded.getName();
		int nbParams = decoded.getNbParameters();
		String[] result = new String[nbParams];
		if(nbParams > 0) {
			int stackIndex = 0;
			if(frame.stackBefore.stack.get(stackIndex) instanceof TrackableArray) {
				StackElement[] varargs = ((TrackableArray) frame.stackBefore.stack.get(0)).elements;
				nbParams = nbParams + varargs.length - 1;
				result = new String[nbParams];
				for(int i = 0; i < varargs.length; i++, nbParams--) {
					result[nbParams - 1] = getLocalVariableName(varargs[i]) + "(" + varargs[i] + ")";
				}
				stackIndex++;
			}
			while(nbParams > 0) {
				StackElement se = frame.stackBefore.stack.get(stackIndex++);
				if(se instanceof TOP)
					se = frame.stackBefore.stack.get(stackIndex++);
				result[nbParams - 1] = getLocalVariableName(se) + "(" + se + ")";
				nbParams--;
			}
		}
		return result;
	}
}