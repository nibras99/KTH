  # labwork.S
  # Written 2015 by F Lundevall
  # Skeleton file for IS1200/IS1500 lab 1 2015-2016.
  # The macros PUSH and POP are in the public domain.
.data
.macro	PUSH reg
	addi	$sp,$sp,-4
	sw	\reg,0($sp)
.endm

.macro	POP reg
	lw	\reg,0($sp)
	addi	$sp,$sp,4
.endm

.text
.global delay, time2string
  # You can add your own code here

  # timetemplate.asm
  # Written 2015 by F Lundevall
  # Copyright abandonded - this file is in the public domain.

	.align 2

mytime:	.word 0x5957
timstr:	.ascii

	.text

  # you can write your code for subroutine "hexasc" below this line
hexasc: 
	andi $a0, $a0, 15
	slti $t0, $a0, 10	# sätter t0 till 1 ifall a0 är mindre än 10
	beqz $t0, letters	# om t0 är 0 så hoppar den till letters
	nop
	addi $v0, $a0, 48
	jr $ra
 	nop
 	
letters:  
	addi    $v0,$a0,55     
	jr  $ra
	nop

delay:	
	blez $a0, skip
	nop
	
	add $t4, $a0, $0  # a0 är input (antal ms)
	
	

	li $t2, 20000	# t2 = delaytime (1 ms)
	while:
        addi $t1, $0, 0		# t1 = i = 0
	loop: 
		addi $t1, $t1, 1	# i++
		slt $t0, $t1, $t2 	# true om i < 4711
		bnez $t0, loop
		nop
		
	addi $t4, $t4, -1
	bnez $t4, while
	nop
	
	skip:
	
	jr $ra 
	nop

time2string:
	PUSH $ra
	PUSH $s0 # X0:00
	PUSH $s1 # 0X:00
	PUSH $s2 # 00:X0
	PUSH $s3 # 00:0X
    PUSH $s4 # a1
	PUSH $a0

    #time input ligger i a1
    # X (00:00X)
    andi $t4, $a1, 0x00ff
    andi $t3, $0, 0x00
    bnez $t4, normal
    addi $t3, $0, 0x58

    addi $t5, $0, 0x00
    sb  $t5,6($a0)

	normal:
	andi $a1, $a1, 0xffff	# maskar bort talen innan

    move $s4, $a1


	# X0:00
	srl $a0, $s4, 12	# shiftar så man kan skriva talen på rätt plats
	jal hexasc
	nop 		# anropar hexasc för att rätt tecken ska skrivas ut
	move $s0, $v0		# flyttar tecknet från v0 till rätt plats 
	
	# 0X:00
	srl $a0, $s4, 8
	jal hexasc
	nop	
	move $s1, $v0
	
	# 00:X0
	srl $a0, $s4, 4
	jal hexasc	
	nop
	move $s2, $v0
	
	# 00:0X
	add $a0, $s4, $0
	jal hexasc	
	nop
	move $s3, $v0
	
	POP $a0
	
	sb $s0, 0($a0)
	sb $s1, 1($a0)
	
	addi $t0, $0 ,0x3A
	sb $t0, 2($a0)
	
	sb $s2, 3($a0)
	sb $s3, 4($a0)

    add $t1, $0, $t3
    sb  $t1,5($a0)
	
    POP $s4
	POP $s3
	POP $s2
	POP $s1
	POP $s0
	POP $ra
	
	jr $ra
	nop