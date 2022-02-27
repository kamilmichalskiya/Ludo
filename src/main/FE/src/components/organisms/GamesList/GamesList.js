import React, { useState, useEffect } from 'react';
import GamesListItem from 'components/molecules/GamesListItem/GamesListItem';
import { StyledList, StyledTitle, Wrapper } from './GamesList.styles';
import Form from '../Form/Form';
import { Redirect } from 'react-router-dom';
import Stomp from 'stompjs';

const GamesList = () => {
  const [gamesList, setGamesList] = useState([]);
  const [isLoading, setLoadingState] = useState(false);
  const [gameId, setGameId] = useState('');
  const [gameIndex, setGameIndex] = useState(0);
  const [userName, setUserName] = useState('');
  const [shouldRedirect, setRedirect] = useState(false);
  const [activeGame, setActiveGame] = useState({});
  const [clientConnection, setClientConnection] = useState(null);
  const [channels, setChannels] = useState(new Map());
  let client = '';

  useEffect(() => {
    setLoadingState(true);
    getAllActiveGames();
  }, []);

  useEffect(() => {
    setLoadingState(false);
    console.log('DEBUG: GameBoard: isLoading', isLoading);
  }, [isLoading]);

  useEffect(() => {
    if (gamesList?.length !== 0) {
      console.log('DEBUG: GamesList: gamesList param state updated:', gamesList);
    }
  }, [gamesList]);

  useEffect(() => {
    if (activeGame && Object.keys(activeGame).length !== 0) {
      setRedirect(true);
    }
  }, [activeGame]);

  useEffect(() => {
    if (gamesList.length !== 0) {
      setGamesList(gamesList);
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [gamesList]);

  useEffect(() => {
    if (gameIndex > 0) {
      joinGame(gameId, gameIndex);
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [gameIndex]);

  const getAllActiveGames = async () => {
    setLoadingState(true);
    console.log('DEBUG: GameList: getAllActiveGames');
    let path = '';
    if (window.location.port === '3000') {
      path = 'http://localhost:8080';
    }
    const response = await fetch(path + '/api/games');
    const data = await response.json();
    setGamesList(data);
    setLoadingState(false);
  };

  const createConnection = (id = 'all') => {
    // client = Stomp.client('wss://chinczyk4.herokuapp.com/queue');
    // client = Stomp.client("ws://localhost:8080/queue");
    let path = 'ws://';
    if (window.location.protocol === 'https:') {
      path = 'wss://';
    }
    if (window.location.port === '3000') {
      // path += 'localhost:8080';
      return; // clear console errors for FE development
    } else {
      path += window.location.host;
    }
    client = Stomp.client(path + '/queue');
    setClientConnection(client);
    console.log('DEBUG: Stomp connect');
    client.connect(
      {},
      function (frame) {
        subscribe(id);
      },
      function (frame) {
        createConnection();
      }
    );
  };

  const subscribe = (id = 'all') => {
    const client2 = client ? client : clientConnection;
    const subscription = client2.subscribe('/game/' + id, (message) => {
      if (message) {
        console.log(`DEBUG: Websocket returned value: ${message}`);
        if (gamesList.length !== 0) {
          const newGamesList = [...gamesList];
          const newGame = JSON.parse(message.body);
          for (const game of newGamesList) {
            if (game.id === newGame.id) {
              newGamesList[game] = newGame;
              setGamesList(newGamesList);
              return;
            }
          }
          newGamesList.push(newGame);
          setGamesList(newGamesList);
        }
        setChannels(new Map(channels.set(id, subscription)));
      }
    });
  };

  const unsubscribe = (id = 'all') => {
    console.log('DEBUG: GamesList: unsubscribe');
    const subscription = channels.get(id);
    if (subscription) {
      subscription.unsubscribe();
      setChannels(new Map());
    }
  };

  const createGameHandler = async () => {
    if (userName) {
      const requestOptions = {
        method: 'POST',
      };
      let path = '';
      if (window.location.port === '3000') {
        path = 'http://localhost:8080';
      }
      const response = await fetch(path + '/api/games/new', requestOptions);
      const data = await response.json();
      console.log('DEBUG: GamesList: CreateGameHandler: ', data);
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

  const joinGame = async (gameId, index = gameIndex) => {
    if (userName && gameId) {
      console.log(`DEBUG: GamesList: joinGame with gameId: ${gameId}`);
      const requestOptions = {
        method: 'PUT',
        body: {
          gameId,
          nick: userName,
        },
      };
      let path = '';
      if (window.location.port === '3000') {
        path = 'http://localhost:8080';
      }
      const response = await fetch(path + `/api/players/${userName}/join/${gameId}`, requestOptions);
      const data = await response.json();
      console.log('DEBUG: GamesList: join game: ', data);
      // setGamesList((gamesList) => [...gamesList, data]);
      data.userName = userName;
      setActiveGame(data);
      setGameIndex(index);
      unsubscribe();
      // subscribe(data.id);
      setRedirect(true);
      // createConnection(data.id);
    } else if (!userName) {
      console.warn('Please provide username!');
    }
  };

  const handleInputChange = (e) => {
    if (e.target.value) {
      setUserName(e.target.value);
    }
  };

  return (
    <>
      {shouldRedirect ? <Redirect push to={{ pathname: '/game-board', state: { activeGame, playerName: userName } }} /> : null}
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
                ? gamesList?.map((gameList, index) => (
                    <GamesListItem key={gameList.id} index={index} userData={gameList} joinGame={joinGame} userName={userName} />
                  ))
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
