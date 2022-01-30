import styled from 'styled-components';

export const StyledBox = styled.div`
  border: 1px solid black;
  width: 50px;
  height: 50px;
  height: ${(props) => props.height};
  width: ${(props) => props.width};
  background: ${(props) => props.color};
  left: ${(props) => props.left};
  top: ${(props) => props.top};
  position: relative;
  z-index: 0;
  visibility: ${(props) => props.visibility};
  box-shadow: 0 5px 15px -10px rgba(0, 0, 0, 0.3);
  background-image: url('../../../assets/photos/czinczin.png');
`;
