from pydantic import BaseModel
from fastapi import FastAPI, HTTPException, status
from typing import Dict, Union

app = FastAPI()

class Poll(BaseModel):
    poll_id: int
    question: str
    votes: Union[Dict[str, int], None] = None

class Vote(BaseModel):
    count: int

polls: Dict[int, Poll] = {}

def check_poll_exists(poll_id: int):
    if poll_id not in polls:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Poll not found")

@app.get("/polls")
async def get_polls():
    return polls

@app.post("/polls", status_code=status.HTTP_201_CREATED)
async def create_poll(poll: Poll):
    if poll.poll_id in polls:
        raise HTTPException(status_code=status.HTTP_409_CONFLICT, detail="Poll already exists")

    if poll.votes is None:
        poll.votes = {}

    polls[poll.poll_id] = poll
    return poll

@app.get("/polls/{poll_id}")
async def get_poll(poll_id: int):
    check_poll_exists(poll_id)
    return polls[poll_id]

@app.put("/polls/{poll_id}")
async def update_poll(poll_id: int, question: str):
    check_poll_exists(poll_id)
    polls[poll_id].question = question
    return polls[poll_id]

@app.put("/polls/{poll_id}/votes/{vote_id}")
async def add_vote(poll_id: int, vote_id: str):
    check_poll_exists(poll_id)
    if vote_id not in polls[poll_id].votes:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Vote not found")
    polls[poll_id].votes[vote_id] += 1
    return {vote_id: polls[poll_id].votes[vote_id]}

@app.delete("/polls/{poll_id}", status_code=status.HTTP_204_NO_CONTENT)
async def delete_poll(poll_id: int):
    check_poll_exists(poll_id)
    del polls[poll_id]

@app.post("/polls/{poll_id}/votes", status_code=status.HTTP_201_CREATED)
async def create_vote(poll_id: int, vote: Dict[str, int]):
    check_poll_exists(poll_id)

    if polls[poll_id].votes is None:
        polls[poll_id].votes = {}

    for color, count in vote.items():
        if color in polls[poll_id].votes:
            raise HTTPException(status_code=status.HTTP_409_CONFLICT, detail="Vote already exists")
        polls[poll_id].votes[color] = count

    return vote

@app.get("/polls/{poll_id}/votes")
async def get_votes(poll_id: int):
    check_poll_exists(poll_id)
    return polls[poll_id].votes if polls[poll_id].votes else {}

@app.get("/polls/{poll_id}/votes/{vote_id}")
async def get_vote(poll_id: int, vote_id: str):
    check_poll_exists(poll_id)
    if vote_id not in polls[poll_id].votes:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Vote not found")
    return {vote_id: polls[poll_id].votes[vote_id]}

@app.put("/polls/update/{poll_id}/votes/{vote_id}")
async def update_vote(poll_id: int, vote_id: str, vote: Vote):
    check_poll_exists(poll_id)
    if vote_id not in polls[poll_id].votes:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Vote not found")
    polls[poll_id].votes[vote_id] = vote.count
    return {vote_id: polls[poll_id].votes[vote_id]}
