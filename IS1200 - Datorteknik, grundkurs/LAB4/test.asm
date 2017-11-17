
.text

	addi $a0,$0,4     # set $a0 to 4
	addi $v0,$a0,2	  # test addi, set $v0
	add  $v0,$v0,$a0  # start of counter. Should be 10
loop:	beq  $v0,$a0,done # test, jump to done. v0 == 10, a0 == 4 start
        addi $v0,$v0,-3   # decrement. Loops twice. 10 < 7 < 4
        beq  $0,$0,loop   # emulating an unconditional jump
done:	add  $0,$0,$0	  # NOP	0
	
#a = 4
#v = 6
#v = 10
#10 == 4, false, inte done
#10 = 7
#7 == 4, false
#7 = 4
# 4 == 4, true, klar
# 0