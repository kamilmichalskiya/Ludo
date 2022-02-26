import React, { useState, useEffect } from 'react';
import Box from 'components/atoms/Box/Box';
import { Label } from 'components/atoms/Label/Label';
import { PrimaryButton } from 'components/atoms/Button/Button';
import { Wrapper, StyledRow, StyledNavigation, StyledGameBoardContainer, HeaderSection } from './GameBoard.styles';
import { Link } from 'react-router-dom';
import Dice from 'react-dice-roll';

const GamesBoard = ({ location: { state } }) => {
  const [isLoading, setLoadingState] = useState(true);
  const [gameData, setGameData] = useState(state.activeGame);
  const [playerInfo, setPlayerInfo] = useState({ playerName: state.playerName, playerId: null, isHisTurn: false, diceResult: 0, moveablePawns: [] });
  const [pawnsObject, setPawnsObject] = useState({});

  useEffect(() => {
    loadPawns();
    setPlayerId();
    setLoadingState(false);
    console.log('DEBUG: GameBoard: isLoading', isLoading);
  }, []);

  useEffect(() => {
    console.log('DEBUG: GameBoard: Pawns Object updated!', pawnsObject);
  }, [pawnsObject]);

  const setPlayerId = () => {
    if (gameData && gameData?.players?.length !== 0 && playerInfo?.playerName) {
      const { players } = gameData;
      for (const player of players) {
        if (player.nick === playerInfo.playerName && player.id) {
          const newPlayerInfo = { ...playerInfo };
          newPlayerInfo.playerId = player.id;
          if (newPlayerInfo.playerId === gameData.nextPlayerId) {
            newPlayerInfo.isHisTurn = true;
          } else {
            newPlayerInfo.isHisTurn = false;
          }
          newPlayerInfo.isHisTurn = true // override
          setPlayerInfo(newPlayerInfo);
        }
      }
    }
  };

  const loadPawns = () => {
    if (gameData) {
      let pawns = { ...pawnsObject };
      if (gameData.players && gameData.players.length === 0) {
        gameData.pawns.forEach((pawn) => {
          pawns[pawn.id] = { color: pawn.color, location: pawn.location };
        });
      } else {
        gameData?.players?.forEach((player) => {
          player.pawns.forEach((pawn) => {
            if (pawn.location.endsWith('_BASE')) {
              if (!isLocationTaken(`${pawn.location}_1`, player)) {
                pawn.location = `${pawn.location}_1`;
              } else if (!isLocationTaken(`${pawn.location}_2`, player)) {
                pawn.location = `${pawn.location}_2`;
              } else if (!isLocationTaken(`${pawn.location}_3`, player)) {
                pawn.location = `${pawn.location}_3`;
              } else if (!isLocationTaken(`${pawn.location}_4`, player)) {
                pawn.location = `${pawn.location}_4`;
              }
            }
            pawns[pawn.id] = { color: pawn.color, location: pawn.location };
          });
        });
      }
      setPawnsObject(pawns);
    }
  };

  const isLocationTaken = (location, player) => {
    let isLocationTaken = false;
    player.pawns.forEach((pawn) => {
      if (pawn.location === location) {
        console.log('DEBUG: Pawn assignment to base: ', location, pawn.location);
        isLocationTaken = true;
      }
    });
    return isLocationTaken;
  };

  const startGame = async () => {
    const requestOptions = {
      method: 'PUT',
      body: {
        gameId: gameData.id,
      },
    };
    let path = '';
    if (window.location.port === '3000') {
      path = 'http://localhost:8080';
    }
    const response = await fetch(path + `/api/games/${gameData.id}/start`, requestOptions);
    const data = await response.json();
    if (data && !data?.error) {
      if (data?.players?.length > 1) {
        console.log('DEBUG: GameBoard: StartGame: ', data);
        setGameData(data);
      } else {
        console.warn('DEBUG: The number of players is not correct!');
      }
    } else {
      console.error('ERROR: GameBoard: StartGame failed!');
    }
  };

  const rollDice = async () => {
    if (gameData && gameData.id && playerInfo?.playerName) {
      if (playerInfo.isHisTurn) {
        console.log(`DEBUG: GameBoard: Roll Dice w/ userName: ${playerInfo.playerName}`);
        let path = '';
        if (window.location.port === '3000') {
          path = 'http://localhost:8080';
        }
        const response = await fetch(path + `api/games/${gameData.id}/player/${playerInfo.playerId}/dice`);
        const data = await response.json();
        if (data && data?.length !== 0) {
          const newPlayerInfo = { ...playerInfo };
          newPlayerInfo.moveablePawns = data;
          setPlayerInfo(newPlayerInfo);
        }
      } else {
        console.log('DEBUG: GameBoard: rollDice: its not your turn!');
      }
    } else {
      console.error(`DEBUG: Gameboard: cannot roll dice w/o uninitialized data: ${gameData}, ${playerInfo.playerName}`);
    }
  };

  const movePawn = () => {
    console.log(`DEBUG: GameBoard: MovePawn`);
  };

  console.log('DEBUG: GameBoard initialize params: ', state);

  return (
    <>
      <Wrapper>
        <h1>{isLoading ? 'Loading...' : ''}</h1>
        <StyledNavigation>
          <PrimaryButton onClick={startGame}>Start Game!</PrimaryButton>
          <PrimaryButton onClick={rollDice}>Roll Dice!</PrimaryButton>
          <Link to="/">
            <PrimaryButton>Leave Game :c</PrimaryButton>
          </Link>
        </StyledNavigation>
        <StyledGameBoardContainer>
          <StyledRow>
            <HeaderSection>
              <Label>{gameData?.players[3]?.nick || 'Player 4'}</Label>
              <Label>{gameData?.players[0]?.nick || 'Player 1'}</Label>
            </HeaderSection>
          </StyledRow>
          <StyledRow>
            <Box type="N" color="yellow" id="Y_BASE_1" pawnsArray={pawnsObject} />
            <Box type="N" color="yellow" id="Y_BASE_2" pawnsArray={pawnsObject} />
            <Box type="N" visibility="hidden" />
            <Box type="N" visibility="hidden" />
            <Box type="N" color="white" id="Y_8" pawnsArray={pawnsObject} />
            <Box type="N" color="white" id="Y_9" pawnsArray={pawnsObject} />
            <Box type="N" color="red" id="R_0" pawnsArray={pawnsObject} />
            <Box type="N" visibility="hidden" />
            <Box type="N" visibility="hidden" />
            <Box type="N" color="red" id="R_BASE_1" pawnsArray={pawnsObject} />
            <Box type="N" color="red" id="R_BASE_2" pawnsArray={pawnsObject} />
          </StyledRow>
          <StyledRow>
            <Box type="N" color="yellow" id="Y_BASE_3" pawnsArray={pawnsObject} />
            <Box type="N" color="yellow" id="Y_BASE_4" pawnsArray={pawnsObject} />
            <Box type="N" visibility="hidden" />
            <Box type="N" visibility="hidden" />
            <Box type="N" color="white" id="Y_7" pawnsArray={pawnsObject} />
            <Box type="N" color="red" id="R_HOME" pawnsArray={pawnsObject} />
            <Box type="N" color="white" id="R_1" pawnsArray={pawnsObject} />
            <Box type="N" visibility="hidden" />
            <Box type="N" visibility="hidden" />
            <Box type="N" color="red" id="R_BASE_3" pawnsArray={pawnsObject} />
            <Box type="N" color="red" id="R_BASE_4" pawnsArray={pawnsObject} />
          </StyledRow>
          <StyledRow>
            <Box type="N" visibility="hidden" />
            <Box type="N" visibility="hidden" />
            <Box type="N" visibility="hidden" />
            <Box type="N" visibility="hidden" />
            <Box type="N" color="white" id="Y_6" pawnsArray={pawnsObject} />
            <Box type="N" color="red" id="R_HOME" pawnsArray={pawnsObject} />
            <Box type="N" color="white" id="R_2" pawnsArray={pawnsObject} />
            <Box type="N" visibility="hidden" />
            <Box type="N" visibility="hidden" />
            <Box type="N" visibility="hidden" />
            <Box type="N" visibility="hidden" />
          </StyledRow>
          <StyledRow>
            <Box type="N" visibility="hidden" />
            <Box type="N" visibility="hidden" />
            <Box type="N" visibility="hidden" />
            <Box type="N" visibility="hidden" />
            <Box type="N" color="white" id="Y_5" pawnsArray={pawnsObject} />
            <Box type="N" color="red" id="R_HOME" pawnsArray={pawnsObject} />
            <Box type="N" color="white" id="R_3" pawnsArray={pawnsObject} />
            <Box type="N" visibility="hidden" />
            <Box type="N" visibility="hidden" />
            <Box type="N" visibility="hidden" />
            <Box type="N" visibility="hidden" />
          </StyledRow>
          {/* Middle Section */}
          <StyledRow>
            <Box type="N" color="yellow" id="Y_0" pawnsArray={pawnsObject} />
            <Box type="N" color="white" id="Y_1" pawnsArray={pawnsObject} />
            <Box type="N" color="white" id="Y_2" pawnsArray={pawnsObject} />
            <Box type="N" color="white" id="Y_3" pawnsArray={pawnsObject} />
            <Box type="N" color="white" id="Y_4" pawnsArray={pawnsObject} />
            <Box type="N" color="red" id="R_HOME" pawnsArray={pawnsObject} />
            <Box type="N" color="white" id="R_4" pawnsArray={pawnsObject} />
            <Box type="N" color="white" id="R_5" pawnsArray={pawnsObject} />
            <Box type="N" color="white" id="R_6" pawnsArray={pawnsObject} />
            <Box type="N" color="white" id="R_7" pawnsArray={pawnsObject} />
            <Box type="N" color="white" id="R_8" pawnsArray={pawnsObject} />
          </StyledRow>
          <StyledRow>
            <Box type="N" color="white" id="B_9" pawnsArray={pawnsObject} />
            <Box type="N" color="yellow" id="Y_HOME" pawnsArray={pawnsObject} />
            <Box type="N" color="yellow" id="Y_HOME" pawnsArray={pawnsObject} />
            <Box type="N" color="yellow" id="Y_HOME" pawnsArray={pawnsObject} />
            <Box type="N" color="yellow" id="Y_HOME" pawnsArray={pawnsObject} />
            <Dice size="50" placement="top-right" disabled={true}></Dice>
            <Box type="N" color="green" id="G_HOME" pawnsArray={pawnsObject} />
            <Box type="N" color="green" id="G_HOME" pawnsArray={pawnsObject} />
            <Box type="N" color="green" id="G_HOME" pawnsArray={pawnsObject} />
            <Box type="N" color="green" id="G_HOME" pawnsArray={pawnsObject} />
            <Box type="N" color="white" id="R_9" pawnsArray={pawnsObject} />
          </StyledRow>
          <StyledRow>
            <Box type="N" color="white" id="B_8" pawnsArray={pawnsObject} />
            <Box type="N" color="white" id="B_7" pawnsArray={pawnsObject} />
            <Box type="N" color="white" id="B_6" pawnsArray={pawnsObject} />
            <Box type="N" color="white" id="B_5" pawnsArray={pawnsObject} />
            <Box type="N" color="white" id="B_4" pawnsArray={pawnsObject} />
            <Box type="N" color="blue" id="B_HOME" pawnsArray={pawnsObject} />
            <Box type="N" color="white" id="G_4" pawnsArray={pawnsObject} />
            <Box type="N" color="white" id="G_3" pawnsArray={pawnsObject} />
            <Box type="N" color="white" id="G_2" pawnsArray={pawnsObject} />
            <Box type="N" color="white" id="G_1" pawnsArray={pawnsObject} />
            <Box type="N" color="green" id="G_0" pawnsArray={pawnsObject} />
          </StyledRow>
          {/* Lower Section */}
          <StyledRow>
            <Box type="N" visibility="hidden" />
            <Box type="N" visibility="hidden" />
            <Box type="N" visibility="hidden" />
            <Box type="N" visibility="hidden" />
            <Box type="N" color="white" id="B_3" pawnsArray={pawnsObject} />
            <Box type="N" color="blue" id="B_HOME" pawnsArray={pawnsObject} />
            <Box type="N" color="white" id="G_5" pawnsArray={pawnsObject} />
            <Box type="N" visibility="hidden" />
            <Box type="N" visibility="hidden" />
            <Box type="N" visibility="hidden" />
            <Box type="N" visibility="hidden" />
          </StyledRow>
          <StyledRow>
            <Box type="N" visibility="hidden" />
            <Box type="N" visibility="hidden" />
            <Box type="N" visibility="hidden" />
            <Box type="N" visibility="hidden" />
            <Box type="N" color="white" id="B_2" pawnsArray={pawnsObject} />
            <Box type="N" color="blue" id="B_HOME" pawnsArray={pawnsObject} />
            <Box type="N" color="white" id="G_6" pawnsArray={pawnsObject} />
            <Box type="N" visibility="hidden" />
            <Box type="N" visibility="hidden" />
            <Box type="N" visibility="hidden" />
            <Box type="N" visibility="hidden" />
          </StyledRow>
          <StyledRow>
            <Box type="N" color="blue" id="B_BASE_1" pawnsArray={pawnsObject} />
            <Box type="N" color="blue" id="B_BASE_2" pawnsArray={pawnsObject} />
            <Box type="N" visibility="hidden" />
            <Box type="N" visibility="hidden" />
            <Box type="N" color="white" id="B_1" pawnsArray={pawnsObject} />
            <Box type="N" color="blue" id="B_HOME" pawnsArray={pawnsObject} />
            <Box type="N" color="white" id="G_7" pawnsArray={pawnsObject} />
            <Box type="N" visibility="hidden" />
            <Box type="N" visibility="hidden" />
            <Box type="N" color="green" id="G_BASE_1" pawnsArray={pawnsObject} />
            <Box type="N" color="green" id="G_BASE_2" pawnsArray={pawnsObject} />
          </StyledRow>
          <StyledRow>
            <Box type="N" color="blue" id="B_BASE_3" pawnsArray={pawnsObject} />
            <Box type="N" color="blue" id="B_BASE_4" pawnsArray={pawnsObject} />
            <Box type="N" visibility="hidden" />
            <Box type="N" visibility="hidden" />
            <Box type="N" color="blue" id="B_0" pawnsArray={pawnsObject} />
            <Box type="N" color="white" id="G_8" pawnsArray={pawnsObject} />
            <Box type="N" color="white" id="G_9" pawnsArray={pawnsObject} />
            <Box type="N" visibility="hidden" />
            <Box type="N" visibility="hidden" />
            <Box type="N" color="green" id="G_BASE_3" pawnsArray={pawnsObject} />
            <Box type="N" color="green" id="G_BASE_4" pawnsArray={pawnsObject} />
          </StyledRow>
          <StyledRow>
            <HeaderSection>
              <Label>{gameData?.players[2]?.nick || 'Player 3'}</Label>
              <Label>{gameData?.players[1]?.nick || 'Player 2'}</Label>
            </HeaderSection>
          </StyledRow>
        </StyledGameBoardContainer>
      </Wrapper>
    </>
  );
};

export default GamesBoard;
