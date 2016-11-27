class Register
{
	String regName="";
	boolean isRob;
}

class Instruction
{
	Register dst;
	Register src1;
	Register src2;
	int opType;
	int ipc=0;

}