4.1 Possibility 1 — Replicated Concurrency Control and Re- covery (RepCRec for short)
Implement a distributed concurrency control algorithm and commit algo- rithm with replication. Variables x1, ..., x20 (that is, there are only 20 variables in whole database — the numbers between 1 and 20 will be re- ferred to as indexes below). Sites are 1 to 10. A copy is indicated by a dot.
4
Thus, x6.2 is the copy of variable x6 at site 2. The odd indexed variables are at one site each (i.e. 1 + index number mod 10 ). Even indexed variables are at all sites. Each variable xi is initialized to the value 10i.
Implement the available copies approach to replication using two phase locking (using read and write locks) at each site and validation at commit time.
Avoid deadlocks using the wait-die protocol in which older transactions wait for younger ones, but younger ones never wait for older ones. No two transactions will have the same age. This implies that your system must keep track of the oldest transaction time of any transaction holding a lock.
(Possible optimization: If T2 is waiting for a lock on x and T3 later arrives and is also waiting for a lock on x and T3 is younger than T2 and the lock T2 wants conflicts with the lock that T3 wants, then you may if you wish abort T3 right away. Alternatively, you can delay the decision until T2 actually acquires the lock and abort T3 then.)
Read-only transactions should use multiversion read consistency.
Input instructions come from a file in or the standard input, output goes to a file out. (That means your algorithms may not look ahead in the input.) Input instructions occurring in one step begin at a new line and end with a carriage return. Thus, there will be several operations in each step, though at most only one per transaction. Some of these operations may be blocked due to conflicting locks. You may assume that the processors work in lock-step That is, you may assume that all operations on a single line occur concurrently. When running our tests, we will ensure that operations occurring concurrently they don’t conflict with one another.
Input is of the form:
begin(T1) says that T1 begins beginRO(T3) says that T3 is read-only
R(T1, x4) says transaction 1 wishes to read x4 (provided it can get the locks or provided it doesn’t need the locks (if T1 is a read-only transaction)). It should read any up (i.e. alive) copy and return the current value.
W(T1, x6,v) says transaction 1 wishes to write all available copies of x6 (provided it can get the locks) with the value v.
dump() gives the committed values of all copies of all variables at all sites, sorted per site.
5
dump(i) gives the committed values of all copies of all variables at site i.
dump(xj) gives the committed values of all copies of variable xj at all sites.
end(T1) causes your system to report whether T1 can commit.
fail(6) says site 6 fails. (This is not issued by a transaction, but is just an event that the tester will execute.)
recover(7) says site 7 recovers. (Again, a tester-caused event) We discuss this further below.
A newline means time advances by one. A semicolon is a separator for co-temporous events.
Example (partial script with six steps in which transactions T1 and T2 commit, and one of T3 and T4 may commit)
begin(T1) begin(T2)
begin(T3)
W(T1, x1,5); W(T3, x2,32)
W(T2, x1,17); — will cause T2 to die because it cannot wait for an older lock
end(T1); begin(T4)
W(T4, x4,35); W(T3, x5,21)
W(T4,x2,21); W(T3,x4,23) — T4 will die freeing the lock on x4 allowing T3 to finish
Your program should consist of two parts: a single transaction manager that translates read and write requests on variables to read and write re- quests on copies using the available copy algorithm described in the notes. The transaction manager never fails. (Having a single global transaction manager that never fails is a simplification of reality, but it is not too hard to get rid of that assumption.)
If the TM requests a read for transaction T and cannot get it due to failure, the TM should try another site (all in the same step). If no relevant site is available, then T must wait. T may also have to wait for conflicting locks. Thus the TM may accumulate an input command for T and will try it on the next tick (time moment). While T is blocked (whether waiting for a lock to be released or a failure to be cleared), no new operations for T will
6
appear, so the buffer size for messages from any single transaction can be of size 1.
A data and lock manager at each site performs concurrency control. You should implement a simple message buffer at each site. In one step each working DM reads its message buffer from the TM in that step, performs some processing and perhaps responds to the TM. The TM won’t send more than one message to a DM in one step though that message may contain several operations each from a different transaction.
Failures are indicated only by the fail statement. The site should forget any previous messages sent to it (because these are held in volatile storage) and should forget lock information.
If a site fails and recovers, the DM would normally perform local recov- ery first (perhaps by asking the TM about transactions that the DM holds pre-committed but not yet committed), but this is unnecessary since, in the simulation model, commits are atomic with respect to failures. Therefore, all non-replicated variables are available for reads and writes. Regarding replicated variables, the site makes them available for writing, but not read- ing. In fact, reads will not be allowed until a committed write takes place (see notes on recovery when using the available copies algorithm).
During execution, your program should say which transactions commit and which abort and for what reason. For debugging purposes you should implement the command querystate() which will give the state of each DM and the TM as well as the data distribution and data values. Finally, each read that occurs should show the value read.
