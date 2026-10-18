import React from 'react';
import { Box, Flex, Avatar, Text, useColorModeValue } from '@chakra-ui/react';
import ReactMarkdown from 'react-markdown';

const ChatMessage = ({ message, isUser }) => {
  const bgColor = useColorModeValue('white', 'gray.700');
  const userBg = useColorModeValue('primary.50', 'primary.900');
  const borderColor = useColorModeValue('gray.200', 'gray.600');

  return (
    <Flex
      direction="column"
      mb={4}
      className="message-fade-in"
      maxW="100%"
      {...(isUser ? { alignItems: 'flex-end' } : { alignItems: 'flex-start' })}
    >
      <Flex
        direction="row"
        alignItems="flex-start"
        maxW="90%"
        {...(isUser ? { flexDir: 'row-reverse' } : {})}
      >
        {/* Avatar */}
        <Avatar
          size="sm"
          name={isUser ? 'Utilisateur' : 'Agentic AI'}
          bg={isUser ? 'primary.500' : 'teal.500'}
          color="white"
          boxShadow="sm"
          mt={1}
        />

        {/* Contenu du message */}
        <Box
          bg={isUser ? userBg : bgColor}
          p={3}
          borderRadius="lg"
          borderWidth={isUser ? '0' : '1px'}
          borderColor={borderColor}
          boxShadow="sm"
          ml={isUser ? 0 : 3}
          mr={isUser ? 3 : 0}
          maxW="100%"
          overflowWrap="break-word"
        >
          <ReactMarkdown>
            {message}
          </ReactMarkdown>
        </Box>
      </Flex>

      {/* Horodatage */}
      <Text
        fontSize="xs"
        color="gray.500"
        mt={1}
        px={3}
        textAlign={isUser ? 'right' : 'left'}
      >
        {new Date().toLocaleTimeString('fr-FR', { hour: '2-digit', minute: '2-digit' })}
      </Text>
    </Flex>
  );
};

export default ChatMessage;
