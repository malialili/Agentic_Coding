import React from 'react';
import { Box, Heading, Text, Badge, Flex, IconButton, useColorModeValue } from '@chakra-ui/react';
import { FiTrash2, FiEdit, FiFile } from 'react-icons/fi';

const KnowledgeCard = ({ document, onDelete, onEdit }) => {
  const bgColor = useColorModeValue('white', 'gray.700');
  const borderColor = useColorModeValue('gray.200', 'gray.600');

  return (
    <Box
      bg={bgColor}
      border="1px"
      borderColor={borderColor}
      borderRadius="lg"
      p={4}
      mb={4}
      boxShadow="sm"
      _hover={{ boxShadow: 'md' }}
      transition="box-shadow 0.2s"
    >
      <Flex justify="space-between" align="flex-start" mb={3}>
        <Flex direction="column" flex={1}>
          <Flex align="center" mb={2}>
            <IconButton
              aria-label="Type de document"
              icon={<FiFile />}
              variant="ghost"
              size="sm"
              mr={2}
            />
            <Heading as="h3" size="md" color="gray.800">
              {document.title}
            </Heading>
          </Flex>

          <Text fontSize="sm" color="gray.500" mb={2}>
            {document.category}
          </Text>
        </Flex>

        <Flex direction="row">
          <IconButton
            aria-label="Modifier"
            icon={<FiEdit />}
            variant="ghost"
            size="sm"
            mr={1}
            onClick={() => onEdit(document)}
          />
          <IconButton
            aria-label="Supprimer"
            icon={<FiTrash2 />}
            variant="ghost"
            size="sm"
            colorScheme="red"
            onClick={() => onDelete(document.id)}
          />
        </Flex>
      </Flex>

      <Text fontSize="sm" color="gray.600" mb={3} noOfLines={2}>
        {document.content.substring(0, 150)}...
      </Text>

      <Flex wrap="wrap" gap={2}>
        {document.tags.map((tag, index) => (
          <Badge key={index} colorScheme="primary" px={2} py={1}>
            {tag}
          </Badge>
        ))}
      </Flex>

      <Text fontSize="xs" color="gray.400" mt={3} textAlign="right">
        {new Date(document.createdAt).toLocaleDateString('fr-FR')}
      </Text>
    </Box>
  );
};

export default KnowledgeCard;
