@4
D=A
@SP
A=M
M=D
@SP
M=M+1
@END
0;JMP
(TRUE)
@SP
A=M-1
M=-1
@address
A=M
0;JMP
(FALSE)
@SP
A=M-1
M=0
@address
A=M
0;JMP
(END)
@END
0;JMP
