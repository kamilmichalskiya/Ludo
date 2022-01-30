import React, { useState, useEffect } from 'react';
import GamesListItem from 'components/molecules/GamesListItem/GamesListItem';
import { StyledList, StyledTitle, Wrapper } from './GamesList.styles';
import Form from '../Form/Form';
import { Redirect } from 'react-router-dom';

const GamesList = () => {
  const [gamesList, setGamesList] = useState([]);
  const [isLoading, setLoadingState] = useState(false);
  // eslint-disable-next-line no-unused-vars
  const [gameId, setGameId] = useState('');
  const [gameIndex, setGameIndex] = useState(0);
  const [userName, setUserName] = useState('');
  const [shouldRedirect, setRedirect] = useState(false);

  useEffect(() => {
    setLoadingState(true);
    getAllActiveGames();
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
    const response = await fetch('localhost:8080/games');
    const data = await response.json();
    setGamesList(data);

    setLoadingState(false);
  };

  const createGameHandler = async () => {
    if (userName) {
      const requestOptions = {
        method: 'POST',
      };
      const response = await fetch('localhost:8080/games/new', requestOptions);
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
      const response = await fetch(`localhost:8080/players/${userName}/join/${gameId}`, requestOptions);
      const data = await response.json();
      console.log('GamesList: join game: ', data);
      setGamesList((gamesList) => [...gamesList, data]);
      setGameIndex(index);
      setRedirect(true);
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
