import React, { useState, useEffect } from 'react';
import Box from 'components/atoms/Box/Box';
import { Label } from 'components/atoms/Label/Label';
import { PrimaryButton } from 'components/atoms/Button/Button';
import { Wrapper, StyledRow, StyledNavigation, StyledGameBoardContainer, HeaderSection } from './GameBoard.styles';
import { Link } from 'react-router-dom';
import Dice from 'react-dice-roll';

const GamesBoard = ({ location: { state } }) => {
  const [isLoading, setLoadingState] = useState(true);
  const [gameData, setGameData] = useState(state);
  const [pawnsObject, setPawnsObject] = useState({});

  useEffect(() => {
    loadPawns();
    setLoadingState(false);
    console.log('GameBoard: isLoading', isLoading);
  }, []);

  useEffect(() => {
    console.log('Pawns Object updated!', pawnsObject);
  }, [pawnsObject]);

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
        console.log('Pawn assignment to base: ', location, pawn.location);
        isLocationTaken = true;
      }
    });
    return isLocationTaken;
  };

  const startGame = () => {
    console.log('Start Game!');
  };

  const rollDice = () => {
    console.log('Roll Dice!');
  };

  console.log('GameBoard initialize params: ', state);

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
              <Label>{gameData?.players[1]?.nick || 'Player 2'}</Label>
              <Label>{gameData?.players[2]?.nick || 'Player 3'}</Label>
            </HeaderSection>
          </StyledRow>
        </StyledGameBoardContainer>
      </Wrapper>
    </>
  );
};

export default GamesBoard;
