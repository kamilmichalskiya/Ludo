import React, { useState, useEffect } from 'react';
import Box from 'components/atoms/Box/Box';
import { Label } from 'components/atoms/Label/Label';
import { PrimaryButton } from 'components/atoms/Button/Button';
import { Wrapper, StyledRow, StyledNavigation, StyledGameBoardContainer, HeaderSection } from './GameBoard.styles';
import { Link } from 'react-router-dom';
import Dice from 'react-dice-roll';
import Stomp from 'stompjs';
import rollDiceVideo1 from '../../../assets/gifs/roll_dice_1.mp4';
import rollDiceVideo2 from '../../../assets/gifs/roll_dice_2.mp4';
import rollDiceVideo3 from '../../../assets/gifs/roll_dice_3.mp4';
import rollDiceVideo4 from '../../../assets/gifs/roll_dice_4.mp4';
import rollDiceVideo5 from '../../../assets/gifs/roll_dice_5.mp4';
import rollDiceVideo6 from '../../../assets/gifs/roll_dice_6.mp4';

const GamesBoard = ({ location: { state } }) => {
  const [isLoading, setLoadingState] = useState(true);
  const [gameData, setGameData] = useState(state.activeGame);
  const [playerInfo, setPlayerInfo] = useState({
    playerName: state.playerName,
    playerId: null,
    color: null,
    isHisTurn: false,
    diceResult: 1,
    pawns: [],
    moveablePawns: [],
  });
  const [diceResult, setDiceResult] = useState([0]);
  const [clientConnection, setClientConnection] = useState(null);
  const [channels, setChannels] = useState(new Map());
  const [rollChannels, setRollChannels] = useState(new Map());
  const [pawnsObject, setPawnsObject] = useState({});
  const [playVideoState, setPlayVideoState] = useState(false);
  let client = '';

  useEffect(() => {
    loadPawns();
    setPlayerId();
    findNextPlayer();
    createConnection(gameData.id);
    setLoadingState(false);
    console.log('DEBUG: GameBoard: isLoading', isLoading);
  }, []);

  useEffect(() => {
    console.log('DEBUG: GameBoard: Pawns Object updated!', pawnsObject);
  }, [pawnsObject]);

  useEffect(() => {
    if (diceResult[0] > 0) {
      console.log('Set play video state', new Date());
      setPlayVideoState(true);
    }
  }, [diceResult]);

  useEffect(() => {
    if (gameData) {
      loadPawns();
      setPlayerId();
    }
  }, [gameData]);

  const createConnection = (id = 'all') => {
    let path = 'ws://';
    if (window.location.protocol === 'https:') {
      path = 'wss://';
    }
    if (window.location.port === '3000') {
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
        createConnection(gameData.id);
      }
    );
  };

  const subscribe = (id = 'all') => {
    const client2 = client ? client : clientConnection;
    const subscription = client2.subscribe('/game/' + id, (message) => {
      if (message) {
        console.log('Set gameData state', new Date());
        setGameData(JSON.parse(message.body));
      }
    });
    const rollSubscription = client2.subscribe('/roll/' + id, (message) => {
      if (message) {
        const newPlayerInfo = { ...playerInfo };
        newPlayerInfo.diceResult = [JSON.parse(message.body)];
        console.log('Set playerInfo state', new Date());
        setPlayerInfo(newPlayerInfo);
        setDiceResult([JSON.parse(message.body)]);
      }
    });
    setChannels(new Map(channels.set(id, subscription)));
    setRollChannels(new Map(rollChannels.set(id, rollSubscription)));
  };

  const unsubscribe = (id = 'all') => {
    console.log('DEBUG: GamesList: unsubscribe');
    const subscription = channels.get(id);
    const rollSubscription = rollChannels.get(id);
    if (subscription) {
      subscription.unsubscribe();
      setChannels(new Map());
    }
    if (rollSubscription) {
      rollSubscription.unsubscribe();
      setRollChannels(new Map());
    }
    // clientConnection.disconnect();
  };

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
          newPlayerInfo.color = player.pawns[0].color;
          newPlayerInfo.pawns = player.pawns;
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
            } else if (pawn.location.endsWith('_HOME')) {
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

  const movePawn = async (pawnLocation) => {
    if (pawnLocation && playerInfo.isHisTurn) {
      for (const pawn in pawnsObject) {
        if (pawnsObject[pawn].location === pawnLocation) {
          const pawnId = pawn;
          console.log(`DEBUG: GameBoard: movePawn w/ ${pawnId}`);
          const requestOptions = {
            method: 'PUT',
            body: {
              distance: diceResult[0],
              pawnId,
            },
          };
          let path = '';
          if (window.location.port === '3000') {
            path = 'http://localhost:8080';
          }
          const response = await fetch(path + `/api/pawns/${pawnId}/move/${diceResult[0]}`, requestOptions);
          const data = await response.json();
          setDiceResult([0]);
          console.log(`DEBUG: GameBoard: movePawn: ${pawnId} ${diceResult} Result ${data}`);
          // getGame();
        }
      }
    }
  };

  const getGame = async () => {
    let path = '';
    if (window.location.port === '3000') {
      path = 'http://localhost:8080';
    }
    const response = await fetch(path + `/api/games/${gameData.id}`);
    const data = await response.json();
    const newGameData = { ...data };
    setGameData(newGameData);
  };

  const findNextPlayer = () => {
    if (gameData && playerInfo) {
      const { players } = gameData;
      for (const player of players) {
        if (gameData.nextPlayerId === player.id) {
          // const newGameData = { ...gameData };
          // newGameData.nextPlayerName = player.nick;
          // setGameData(newGameData);
          return player.nick;
        }
      }
    }
  };

  const findPlayerName = (color) => {
    if (gameData) {
      const { players } = gameData;
      for (const player of players) {
        if (player?.pawns[0].color === color) {
          return player.nick;
        }
      }
    }
  };

  const onRollDiceVideoEnd = () => {
    console.log('DEBUG: GameBoard: VideoEnd');
    setPlayVideoState(false);
  };

  const playRollDiceVideo = (number = diceResult[0]) => {
    let video;
    switch (number) {
      case 1:
        video = rollDiceVideo1;
        break;
      case 2:
        video = rollDiceVideo2;
        break;
      case 3:
        video = rollDiceVideo3;
        break;
      case 4:
        video = rollDiceVideo4;
        break;
      case 5:
        video = rollDiceVideo5;
        break;
      case 6:
        video = rollDiceVideo6;
        break;
      default:
        break;
    }
    return video;
  };

  return (
    <>
      <Wrapper>
        <h1>{isLoading ? 'Loading...' : ''}</h1>
        <StyledNavigation>
          <PrimaryButton onClick={startGame}>Start Game!</PrimaryButton>
          <PrimaryButton onClick={rollDice}>Roll Dice!</PrimaryButton>
          <Link to="/">
            <PrimaryButton>Leave Game</PrimaryButton>
          </Link>
        </StyledNavigation>

        {playVideoState ? (
          <video className="image" alt="dice-roll" onEnded={onRollDiceVideoEnd} controls autoPlay width="100%">
            <source src={playRollDiceVideo()} type="video/mp4" />
          </video>
        ) : (
          <StyledGameBoardContainer>
            <StyledRow>
              <HeaderSection>
                <Label>Your nickname: {playerInfo?.playerName || 'null'}</Label>
                <Label>Next Player: {findNextPlayer() || 'undefined'}</Label>
              </HeaderSection>
            </StyledRow>
            <br></br> <br></br>
            <StyledRow>
              <HeaderSection>
                <Label>{findPlayerName('YELLOW') || 'Player 4'}</Label>
                <Label>{findPlayerName('RED') || 'Player 1'}</Label>
              </HeaderSection>
            </StyledRow>
            <StyledRow>
              <Box type="N" color="yellow" id="Y_BASE_1" pawnsArray={pawnsObject} movePawn={movePawn} />
              <Box type="N" color="yellow" id="Y_BASE_2" pawnsArray={pawnsObject} movePawn={movePawn} />
              <Box type="N" visibility="hidden" />
              <Box type="N" visibility="hidden" />
              <Box type="N" color="white" id="Y_8" pawnsArray={pawnsObject} movePawn={movePawn} />
              <Box type="N" color="white" id="Y_9" pawnsArray={pawnsObject} movePawn={movePawn} />
              <Box type="N" color="red" id="R_0" pawnsArray={pawnsObject} movePawn={movePawn} />
              <Box type="N" visibility="hidden" />
              <Box type="N" visibility="hidden" />
              <Box type="N" color="red" id="R_BASE_1" pawnsArray={pawnsObject} movePawn={movePawn} />
              <Box type="N" color="red" id="R_BASE_2" pawnsArray={pawnsObject} movePawn={movePawn} />
            </StyledRow>
            <StyledRow>
              <Box type="N" color="yellow" id="Y_BASE_3" pawnsArray={pawnsObject} movePawn={movePawn} />
              <Box type="N" color="yellow" id="Y_BASE_4" pawnsArray={pawnsObject} movePawn={movePawn} />
              <Box type="N" visibility="hidden" />
              <Box type="N" visibility="hidden" />
              <Box type="N" color="white" id="Y_7" pawnsArray={pawnsObject} movePawn={movePawn} />
              <Box type="N" color="red" id="R_HOME_1" pawnsArray={pawnsObject} movePawn={movePawn} />
              <Box type="N" color="white" id="R_1" pawnsArray={pawnsObject} movePawn={movePawn} />
              <Box type="N" visibility="hidden" />
              <Box type="N" visibility="hidden" />
              <Box type="N" color="red" id="R_BASE_3" pawnsArray={pawnsObject} movePawn={movePawn} />
              <Box type="N" color="red" id="R_BASE_4" pawnsArray={pawnsObject} movePawn={movePawn} />
            </StyledRow>
            <StyledRow>
              <Box type="N" visibility="hidden" />
              <Box type="N" visibility="hidden" />
              <Box type="N" visibility="hidden" />
              <Box type="N" visibility="hidden" />
              <Box type="N" color="white" id="Y_6" pawnsArray={pawnsObject} movePawn={movePawn} />
              <Box type="N" color="red" id="R_HOME_2" pawnsArray={pawnsObject} movePawn={movePawn} />
              <Box type="N" color="white" id="R_2" pawnsArray={pawnsObject} movePawn={movePawn} />
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
              <Box type="N" color="white" id="Y_5" pawnsArray={pawnsObject} movePawn={movePawn} />
              <Box type="N" color="red" id="R_HOME_3" pawnsArray={pawnsObject} movePawn={movePawn} />
              <Box type="N" color="white" id="R_3" pawnsArray={pawnsObject} movePawn={movePawn} />
              <Box type="N" visibility="hidden" />
              <Box type="N" visibility="hidden" />
              <Box type="N" visibility="hidden" />
              <Box type="N" visibility="hidden" />
            </StyledRow>
            {/* Middle Section */}
            <StyledRow>
              <Box type="N" color="yellow" id="Y_0" pawnsArray={pawnsObject} movePawn={movePawn} />
              <Box type="N" color="white" id="Y_1" pawnsArray={pawnsObject} movePawn={movePawn} />
              <Box type="N" color="white" id="Y_2" pawnsArray={pawnsObject} movePawn={movePawn} />
              <Box type="N" color="white" id="Y_3" pawnsArray={pawnsObject} movePawn={movePawn} />
              <Box type="N" color="white" id="Y_4" pawnsArray={pawnsObject} movePawn={movePawn} />
              <Box type="N" color="red" id="R_HOME_4" pawnsArray={pawnsObject} movePawn={movePawn} />
              <Box type="N" color="white" id="R_4" pawnsArray={pawnsObject} movePawn={movePawn} />
              <Box type="N" color="white" id="R_5" pawnsArray={pawnsObject} movePawn={movePawn} />
              <Box type="N" color="white" id="R_6" pawnsArray={pawnsObject} movePawn={movePawn} />
              <Box type="N" color="white" id="R_7" pawnsArray={pawnsObject} movePawn={movePawn} />
              <Box type="N" color="white" id="R_8" pawnsArray={pawnsObject} movePawn={movePawn} />
            </StyledRow>
            <StyledRow>
              <Box type="N" color="white" id="B_9" pawnsArray={pawnsObject} movePawn={movePawn} />
              <Box type="N" color="yellow" id="Y_HOME_1" pawnsArray={pawnsObject} movePawn={movePawn} />
              <Box type="N" color="yellow" id="Y_HOME_2" pawnsArray={pawnsObject} movePawn={movePawn} />
              <Box type="N" color="yellow" id="Y_HOME_3" pawnsArray={pawnsObject} movePawn={movePawn} />
              <Box type="N" color="yellow" id="Y_HOME_4" pawnsArray={pawnsObject} movePawn={movePawn} />
              <Dice defaultValue={diceResult[0] || 6} size="50" placement="top-right" disabled={true}></Dice>
              <Box type="N" color="green" id="G_HOME_1" pawnsArray={pawnsObject} movePawn={movePawn} />
              <Box type="N" color="green" id="G_HOME_2" pawnsArray={pawnsObject} movePawn={movePawn} />
              <Box type="N" color="green" id="G_HOME_3" pawnsArray={pawnsObject} movePawn={movePawn} />
              <Box type="N" color="green" id="G_HOME_4" pawnsArray={pawnsObject} movePawn={movePawn} />
              <Box type="N" color="white" id="R_9" pawnsArray={pawnsObject} movePawn={movePawn} />
            </StyledRow>
            <StyledRow>
              <Box type="N" color="white" id="B_8" pawnsArray={pawnsObject} movePawn={movePawn} />
              <Box type="N" color="white" id="B_7" pawnsArray={pawnsObject} movePawn={movePawn} />
              <Box type="N" color="white" id="B_6" pawnsArray={pawnsObject} movePawn={movePawn} />
              <Box type="N" color="white" id="B_5" pawnsArray={pawnsObject} movePawn={movePawn} />
              <Box type="N" color="white" id="B_4" pawnsArray={pawnsObject} movePawn={movePawn} />
              <Box type="N" color="blue" id="B_HOME_4" pawnsArray={pawnsObject} movePawn={movePawn} />
              <Box type="N" color="white" id="G_4" pawnsArray={pawnsObject} movePawn={movePawn} />
              <Box type="N" color="white" id="G_3" pawnsArray={pawnsObject} movePawn={movePawn} />
              <Box type="N" color="white" id="G_2" pawnsArray={pawnsObject} movePawn={movePawn} />
              <Box type="N" color="white" id="G_1" pawnsArray={pawnsObject} movePawn={movePawn} />
              <Box type="N" color="green" id="G_0" pawnsArray={pawnsObject} movePawn={movePawn} />
            </StyledRow>
            {/* Lower Section */}
            <StyledRow>
              <Box type="N" visibility="hidden" />
              <Box type="N" visibility="hidden" />
              <Box type="N" visibility="hidden" />
              <Box type="N" visibility="hidden" />
              <Box type="N" color="white" id="B_3" pawnsArray={pawnsObject} movePawn={movePawn} />
              <Box type="N" color="blue" id="B_HOME_3" pawnsArray={pawnsObject} movePawn={movePawn} />
              <Box type="N" color="white" id="G_5" pawnsArray={pawnsObject} movePawn={movePawn} />
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
              <Box type="N" color="white" id="B_2" pawnsArray={pawnsObject} movePawn={movePawn} />
              <Box type="N" color="blue" id="B_HOME_2" pawnsArray={pawnsObject} movePawn={movePawn} />
              <Box type="N" color="white" id="G_6" pawnsArray={pawnsObject} movePawn={movePawn} />
              <Box type="N" visibility="hidden" />
              <Box type="N" visibility="hidden" />
              <Box type="N" visibility="hidden" />
              <Box type="N" visibility="hidden" />
            </StyledRow>
            <StyledRow>
              <Box type="N" color="blue" id="B_BASE_1" pawnsArray={pawnsObject} movePawn={movePawn} />
              <Box type="N" color="blue" id="B_BASE_2" pawnsArray={pawnsObject} movePawn={movePawn} />
              <Box type="N" visibility="hidden" />
              <Box type="N" visibility="hidden" />
              <Box type="N" color="white" id="B_1" pawnsArray={pawnsObject} movePawn={movePawn} />
              <Box type="N" color="blue" id="B_HOME_1" pawnsArray={pawnsObject} movePawn={movePawn} />
              <Box type="N" color="white" id="G_7" pawnsArray={pawnsObject} movePawn={movePawn} />
              <Box type="N" visibility="hidden" />
              <Box type="N" visibility="hidden" />
              <Box type="N" color="green" id="G_BASE_1" pawnsArray={pawnsObject} movePawn={movePawn} />
              <Box type="N" color="green" id="G_BASE_2" pawnsArray={pawnsObject} movePawn={movePawn} />
            </StyledRow>
            <StyledRow>
              <Box type="N" color="blue" id="B_BASE_3" pawnsArray={pawnsObject} movePawn={movePawn} />
              <Box type="N" color="blue" id="B_BASE_4" pawnsArray={pawnsObject} movePawn={movePawn} />
              <Box type="N" visibility="hidden" />
              <Box type="N" visibility="hidden" />
              <Box type="N" color="blue" id="B_0" pawnsArray={pawnsObject} movePawn={movePawn} />
              <Box type="N" color="white" id="G_8" pawnsArray={pawnsObject} movePawn={movePawn} />
              <Box type="N" color="white" id="G_9" pawnsArray={pawnsObject} movePawn={movePawn} />
              <Box type="N" visibility="hidden" />
              <Box type="N" visibility="hidden" />
              <Box type="N" color="green" id="G_BASE_3" pawnsArray={pawnsObject} movePawn={movePawn} />
              <Box type="N" color="green" id="G_BASE_4" pawnsArray={pawnsObject} movePawn={movePawn} />
            </StyledRow>
            <StyledRow>
              <HeaderSection>
                <Label>{findPlayerName('BLUE') || 'Player 3'}</Label>
                <Label>{findPlayerName('GREEN') || 'Player 2'}</Label>
              </HeaderSection>
            </StyledRow>
          </StyledGameBoardContainer>
        )}
      </Wrapper>
    </>
  );
};

export default GamesBoard;
