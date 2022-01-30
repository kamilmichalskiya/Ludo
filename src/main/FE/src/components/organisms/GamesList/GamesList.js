import React, { useState, useEffect } from 'react';
import GamesListItem from 'components/molecules/GamesListItem/GamesListItem';
import { StyledList, StyledTitle, Wrapper } from './GamesList.styles';
import Form from '../Form/Form';
import { Redirect } from 'react-router-dom';
import Stomp from 'stompjs';

const GamesList = () => {
  const [gamesList, setGamesList] = useState([]);
  const [isLoading, setLoadingState] = useState(false);
  // eslint-disable-next-line no-unused-vars
  const [gameId, setGameId] = useState('');
  const [gameIndex, setGameIndex] = useState(0);
  const [userName, setUserName] = useState('');
  const [shouldRedirect, setRedirect] = useState(false);
  let client = null;
  let subscription = '';

  useEffect(() => {
    setLoadingState(true);
    getAllActiveGames();
    if (!client) {
      createConnection();
    }

    let i = 0;
    while (!client.connected && i <= 10) {
      setTimeout(100);
      i++;
    }
    if (client.connected) {
      subscribe('all');
    }
  }, []);

  useEffect(() => {
    setLoadingState(false);
    console.log('GameBoard: isLoading', isLoading);
  }, [isLoading]);

  useEffect(() => {
    if (gamesList?.length !== 0) {
      console.log('GamesList: gamesList param state updated:', gamesList);
    }
  }, [gamesList]);

  const getAllActiveGames = async () => {
    setLoadingState(true);
    console.log('GameList: getAllActiveGames');
    const response = await fetch('/api/games');
    const data = await response.json();
    setGamesList(data);

    setLoadingState(false);
  };

  const createConnection = () => {
    // client = Stomp.client('wss://chinczyk4.herokuapp.com/queue');
    // client = Stomp.client("ws://localhost:8080/queue");
    let path = "ws://";
    if (window.location.protocol==="https:"){
      path ="wss://"
    }
    path+=window.location.host;
    client = Stomp.client(path + "/queue");
    console.log('Stomp connect');
    client.connect(
        {},
        function (frame) {},
        function (frame) {
          createConnection();
        }
    );
  };

  const subscribe = (id = gameId) => {
    subscription = client.subscribe('/game/' + id, (message) => {
      console.log(`Websocket returned value: ${message}`);
      // let logBody = '';
      // logBody = logBody.concat('|| id: ', body.id);
      // logBody = logBody.concat(' || status: ', body.status);
      // logBody = logBody.concat(' || players: ', body.players.length);
      // logBody = logBody.concat(' || pawns: ', body.pawns.length);
    });
  };

  const createGameHandler = async () => {
    if (userName) {
      const requestOptions = {
        method: 'POST',
      };
      const response = await fetch('/api/games/new', requestOptions);
      const data = await response.json();
      console.log('GamesList: CreateGameHandler: ', data);
      if (data.id) {
        setGameId(data.id);
        setGameIndex(gamesList.length);
      } else {
        console.error('ERROR: gameId was not received!');
      }
    } else {
      console.warn('Please provide username!');
    }
  };

  useEffect(() => {
    if (gameIndex > 0) {
      joinGame(gameId, gameIndex);
    }
  }, [gameIndex]);

  const joinGame = async (gameId, index = gameIndex) => {
    if (userName && gameId) {
      console.log(`GamesList: joinGame with gameId: ${gameId}`);
      const requestOptions = {
        method: 'PUT',
        body: {
          gameId,
          nick: userName,
        },
      };
      const response = await fetch(`/api/players/${userName}/join/${gameId}`, requestOptions);
      const data = await response.json();
      console.log('GamesList: join game: ', data);
      setGamesList((gamesList) => [...gamesList, data]);
      setGameIndex(index);
      setRedirect(true);
      subscribe(data.gameId);
    } else {
      console.warn('Please provide username!');
    }
  };

  const handleInputChange = (e) => {
    if (e.target.value) {
      console.log(e.target.value);
      setUserName(e.target.value);
    }
  };

  return (
    <>
      {shouldRedirect ? <Redirect push to={{ pathname: '/game-board', state: gamesList[gameIndex] }} /> : null}
      <Wrapper>
        <StyledTitle>Welcome to the Lugo Game!</StyledTitle>
        <StyledList>
          {isLoading ? (
            <h3>Your Page is Loading!</h3>
          ) : (
            <>
              <h3>Please enter your nickname*</h3>
              <Form formValues={userName} handleInputChange={handleInputChange}></Form>
              <h3>Then select which game you would like to enter:</h3>
              {gamesList
                ? gamesList?.map((gameList, index) => <GamesListItem key={gameList.id} index={index} userData={gameList} joinGame={joinGame} />)
                : ''}
              <br></br>
              <button onClick={createGameHandler}>Create new game</button>
            </>
          )}
        </StyledList>
      </Wrapper>
    </>
  );
};

export default GamesList;
