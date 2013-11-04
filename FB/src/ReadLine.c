char * readline(){
	static char buffer[100];
	static int  pos, end, current_pos;

	int found_ctr = 0, num;

	char *match = (char*) malloc(100);
	int  buf_size = 100, beg = 0, end = 0;

	while (found_ctr == 0){
		
		if (pos < 0 || pos >= end){
			num = read(buffer,100);
			
			if (num == 0){
				found_ctr = 1;
				break;
			}
			
			else{
				pos = 0;
				end = num;
			}
		}

		//loop to check for \n
		for (cr_pos = pos; buffer[cr_pos] != '\n' && cr_pos <= end; cr_pos++);    

		if (cr_pos <= end){
			if (rbuf_begin + end - pos > rbuf_size){
				rbuf_size += 4096;
				result = (char*)realloc(rbuf_size);
			}

			memcpy(buffer + pos, result + rbuf_begin,  cr_pos - pos);
			rbuf_begin += cr_pos - pos;
			found_cr = 1;
		}
		else{
			if (rbuf_begin + end - pos > rbuf_size){
				rbuf_size += 4096;
				result = (char*)realloc(rbuf_size);
			}
			memcpy(result + rbuf_begin, buffer + pos,  end - pos);
			rbuf_begin += end - pos;
		}
	}

	char * retval = NULL;

	if (found_cr){
		retval = (char*)malloc( rbuf_begin );
		memcpy(retval,  result, rbuf_begin );
	}

	free(result);
	return retval;
}