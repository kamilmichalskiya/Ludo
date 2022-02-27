import React, { useState, useEffect } from 'react';
import YellowPawn from 'assets/icons/yellow_pawn.svg';
import RedPawn from 'assets/icons/red_pawn.svg';
import BluePawn from 'assets/icons/blue_pawn.svg';
import GreenPawn from 'assets/icons/green_pawn.svg';
import { StyledBox, Icon } from './Box.styles';

const Box = (props) => {
  const [pawnsObject, setPawnsArray] = useState(props.pawnsArray);
  const [pawnToBeDisplayed, setPawnsToBeDisplayed] = useState([]);

  useEffect(() => {
    // console.log('Pawns Array in Box component: ', pawnsArray);
  }, []);

  useEffect(() => {
    setPawnsArray(props.pawnsArray);
  }, [props.pawnsArray]);

  useEffect(() => {
    if (pawnsObject) {
      findPawnsToBeDisplayed();
    }
  }, [pawnsObject]);

  useEffect(() => {
    renderSwitch(pawnToBeDisplayed);
  }, [pawnToBeDisplayed]);

  const findPawnsToBeDisplayed = () => {
    if (pawnsObject) {
      for (const [key, value] of Object.entries(pawnsObject)) {
        if (props.id === value.location) {
          setPawnsToBeDisplayed(value.color);
          console.log(`Pawn to be displayed: ', ${value.color}, ${value.location}, key: ${key}`);
        }
      }
    }
  };

  const renderSwitch = () => {
    switch (pawnToBeDisplayed) {
      case 'YELLOW':
        return <Icon src={YellowPawn} alt="YellowPawn" title={props.id} id={props.id} onClick={() => props.movePawn(props.id)} />;
      case 'BLUE':
        return <Icon src={BluePawn} alt="BluePawn" title={props.id} id={props.id} onClick={() => props.movePawn(props.id)} />;
      case 'GREEN':
        return <Icon src={GreenPawn} alt="GreenPawn" title={props.id} id={props.id} onClick={() => props.movePawn(props.id)} />;
      case 'RED':
        return <Icon src={RedPawn} alt="RedPawn" title={props.id} id={props.id} onClick={() => props.movePawn(props.id)} />;
      default:
        break;
    }
  };

  return <StyledBox {...props}>{renderSwitch()}</StyledBox>;
};

export default Box;
