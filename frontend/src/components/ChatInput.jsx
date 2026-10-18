import React, { useState, useRef, useEffect } from 'react';
import { Box, Input, Button, Flex, IconButton, useColorModeValue } from '@chakra-ui/react';
import { FiSend, FiMic, FiPaperclip } from 'react-icons/fi';

const ChatInput = ({ onSend, isLoading }) => {
  const [message, setMessage] = useState('');
  const inputRef = useRef(null);
  const bgColor = useColorModeValue('white', 'gray.700');
  const borderColor = useColorModeValue('gray.200', 'gray.600');

  const handleSend = () => {
    if (message.trim() && !isLoading) {
      onSend(message);
      setMessage('');
    }
  };

  const handleKeyPress = (e) => {
    if (e.key === 'Enter' && !e.shiftKey) {
      e.preventDefault();
      handleSend();
    }
  };

  // Focus automatique
  useEffect(() => {
    inputRef.current?.focus();
  }, [isLoading]);

  return (
    <Box
      p={4}
      bg={bgColor}
      borderTop="1px"
      borderTopColor={borderColor}
      position="sticky"
      bottom={0}
      zIndex={10}
    >
      <Flex direction="row" alignItems="center" maxW="100%">
        {/* Boutons à gauche */}
        <Flex direction="row" mr={2}>
          <IconButton
            aria-label="Joindre un fichier"
            icon={<FiPaperclip />}
            variant="ghost"
            size="lg"
            isDisabled={isLoading}
          />
          <IconButton
            aria-label="Enregistrement vocal"
            icon={<FiMic />}
            variant="ghost"
            size="lg"
            ml={1}
            isDisabled={isLoading}
          />
        </Flex>

        {/* Champ de saisie */}
        <Input
          ref={inputRef}
          flex={1}
          placeholder={isLoading ? "L'agent réfléchit..." : "Posez votre question..."}
          value={message}
          onChange={(e) => setMessage(e.target.value)}
          onKeyPress={handleKeyPress}
          size="lg"
          borderRadius="full"
          bg="gray.100"
          _focus={{ bg: 'white', boxShadow: 'outline' }}
          isDisabled={isLoading}
          pr={12}
        />

        {/* Bouton d'envoi */}
        <Button
          ml={2}
          colorScheme="primary"
          onClick={handleSend}
          isLoading={isLoading}
          loadingText="..."
          size="lg"
          borderRadius="full"
          px={6}
          isDisabled={!message.trim() || isLoading}
          leftIcon={<FiSend />}
        >
          Envoyer
        </Button>
      </Flex>
    </Box>
  );
};

export default ChatInput;
